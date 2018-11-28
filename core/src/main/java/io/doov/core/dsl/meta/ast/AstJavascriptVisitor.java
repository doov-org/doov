/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import org.apache.commons.lang3.StringUtils;

public class AstJavascriptVisitor extends AbstractAstVisitor {

    private final OutputStream ops;
    protected final ResourceProvider bundle;
    protected final Locale locale;

    private int parenthese_depth = 0;   // define the number of parenthesis to close before ending the rule rewriting
    private int start_with_count = 0;   // define the number of 'start_with' rule used for closing parenthesis purpose
    private int end_with_count = 0;     // define the number of 'start_with' rule used for closing parenthesis purpose
    private int use_regexp = 0;         // boolean as an int to know if we are in a regexp for closing parenthesis purpose
    private int is_match=0;             // boolean as an int to know if we are in a matching rule for closing parenthesis purpose
    private int nary_args_count = 0 ;   // define the number of arguments in an N-ary leaf

    public AstJavascriptVisitor(OutputStream ops, ResourceProvider bundle, Locale locale) {
        this.ops = ops;
        this.bundle = bundle;
        this.locale = locale;
    }

    @Override
    public void startMetadata(BinaryMetadata metadata,int depth){
        write("(");
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata,int depth){
        switch((DefaultOperator)metadata.getOperator()){
            case and:
                write(" && ");
                break;
            case or:
                write(" || ");
                break;
            case xor:
                manageXOR(metadata.getLeft(),metadata.getRight());
                break;
            case greater_than:
                write(" > ");
                break;
            case greater_or_equals:
                write(" >= ");
                break;
            case lesser_than:
                write(" < ");
                break;
            case lesser_or_equals:
                write(" <= ");
            case equals:
                write(" == ");
                break;
            case not_equals:
                write(" != ");
                break;
        }
    }

    @Override
    public void endMetadata(BinaryMetadata metadata,int depth){
        write(")");
    }

    @Override
    public void startMetadata(NaryMetadata metadata,int depth){
        if(metadata.getOperator()==match_none) {
            write("!");                             // for predicate [a,b,c] will translate as (!a && !b && !c)
        }
        if(metadata.getOperator()==count || metadata.getOperator()==sum){
            write("[");                             // opening a list to use count or sum on
        }
        if(metadata.getOperator()==min){
            write("Math.min.apply(null,[");         // using JS Math module to apply min on a list, opening the list
        }
    }

    @Override
    public void visitMetadata(NaryMetadata metadata,int depth){
        nary_args_count++;                                      // denying access to the children.size element of a Nary
        if(nary_args_count!=metadata.children().size()) {       // expression, which doesn't exists
            switch((DefaultOperator)metadata.getOperator()){
                case match_any:
                    write(" || ");                          // using 'or' operator to match any of the predicate given
                    break;
                case match_all:
                    write(" && ");                          // using 'and' operator for match all
                    break;
                case match_none:
                    write(" && !");                         // 'and not' for match none
                    break;
                case min:
                case sum:
                case count:
                    write(", ");                            // separating the list values
                    break;
            }
        }
    }

    @Override
    public void endMetadata(NaryMetadata metadata,int depth){
        nary_args_count = 0 ;
        if(metadata.getOperator()==count){
            write("].reduce(function(acc,val){if(val){return acc+1;}return acc;},0)");
        }                                                       // using reduce method to count with an accumulator
        if(metadata.getOperator()==min){
            write("])");                                    // closing the value list
        }
        if(metadata.getOperator()==sum){
            write("].reduce(function(acc,val){ return acc+val;},0)");
        }                                                       // using reduce method to sum the value
    }

    @Override
    public void visitMetadata(LeafMetadata metadata, int depth) {
        if(metadata.flatten().size()>3) {
            ArrayDeque<Element> dateField = new ArrayDeque<>();    //using arrayDeque to store the fields
            ArrayDeque<Element> dateOperator = new ArrayDeque<>(); //using arrayDeque to store the operators
            ArrayDeque<Element> dateValue = new ArrayDeque<>();    //using arrayDeque to store the values
            metadata.stream().forEach(element ->{
                switch(element.getType()){
                    case FIELD:
                        dateField.add(element);
                        break;
                    case TEMPORAL_UNIT:
                    case OPERATOR:
                        dateOperator.add(element);
                        break;
                    case STRING_VALUE:
                    case VALUE:
                        dateValue.add(element);
                        break;
                    case UNKNOWN:
                    case PARENTHESIS_LEFT:
                    case PARENTHESIS_RIGHT:
                        break;
                }
            });
            while(dateOperator.size()>0){
                manageOperator((DefaultOperator)dateOperator.pollFirst().getReadable(),
                        dateField,dateOperator,dateValue);       // calling a method to manage the LeafMD with the deques
            }
        }else {
            metadata.stream().forEach(element -> {
                switch (element.getType()) {
                    case FIELD:
                        write(element.toString());
                        break;
                    case OPERATOR:
                        manageOperator((DefaultOperator) element.getReadable(),null,
                                null,null);
                        break;
                    case VALUE:
                        if(StringUtils.isNumeric(element.toString())) {
                            write(element.toString());
                        }else{
                            write("\'"+element.toString()+"\'");
                        }
                        break;
                    case STRING_VALUE:
                        if(use_regexp==1){
                            String tmp = element.toString();
                            if(is_match==1){
                                is_match=0;
                            }else {
                                tmp = formatRegexp(tmp);
                            }
                            write(tmp);
                            if (start_with_count > 0 && end_with_count==0) {
                                write(".*");
                                start_with_count--;
                            }else if(end_with_count > 0){
                                write("$");
                                end_with_count--;
                            }
                            write("/");
                            use_regexp=0;
                        }else{
                            write("\'"+element.toString()+"\'");
                        }
                        break;
                    case PARENTHESIS_LEFT:
                    case PARENTHESIS_RIGHT:
                    case TEMPORAL_UNIT:
                        write(element.toString());
                        break;
                    case UNKNOWN:
                        write("Unknown " + element.toString());
                        break;
                }
            });

            if (parenthese_depth > 0) {                                 // add parenthesis for each opened
                write(")");
                parenthese_depth--;
            }
        }
    }

    /**
     * managing method for most of the operators in a LeafMetaData context
     * @param element the default operator of the LeafMetadata
     * @param dequeField a deque of the LeafMetadata fields
     * @param dequeOpe a deque of the LeafMetadata intern operators
     * @param dequeValue a deque of the LeafMetadata values
     * */
    private void manageOperator(DefaultOperator element,ArrayDeque<Element> dequeField, ArrayDeque<Element> dequeOpe,
                               ArrayDeque<Element> dequeValue){
        switch(element){
            case rule:
            case validate:
            case empty:
            case as:
            case with:
                break;
            case as_a_number:
                if(dequeField.size()>0) {
                    write("parseInt(");
                    write(dequeField.pollFirst().toString());
                    write(")");
                }
                break;
            case as_string:
                if(dequeField.size()>0){
                    write("String("+dequeField.pollFirst().toString()+")");
                }
                break;
            case not:
                write("!");
                break;
            case always_true:
                write("true");
                break;
            case always_false:
                write("false");
                break;
            case times:
                write(" * ");
                break;
            case equals:
                write(" === ");
                if(dequeValue!=null){
                    write(dequeValue.pollFirst().toString());
                }else if(dequeField!=null){
                    write(dequeField.pollFirst().toString());
                }
                break;
            case not_equals:
                write(" !== ");
                if(dequeValue!=null){
                    write(dequeValue.pollFirst().toString());
                }else if(dequeField!=null){
                    write(dequeField.pollFirst().toString());
                }
                break;
            case is_null:
                write(" === ( null || undefined || \"\" ) ");
                break;
            case is_not_null:
                write(" !== ( null || undefined || \"\" ) ");
                break;
            case minus:
                write(".subtract("+dequeValue.pollFirst()+"," +
                        "\'"+dequeOpe.pollFirst().toString()+"\')");
                break;
            case plus:
                write(".add("+dequeValue.pollFirst()+"," +
                        "\'"+dequeOpe.pollFirst().toString()+"\')");
                break;
            case after:
                write("moment("+dequeField.pollFirst().toString()+"" +
                        ").isAfter(moment("+dequeField.pollFirst().toString()+")");
                parenthese_depth++;
                break;
            case after_or_equals:
                write("moment("+dequeField.pollFirst().toString()+"" +
                        ").isSameOrAfter(moment("+dequeField.pollFirst().toString()+")");
                parenthese_depth++;
                break;
            case age_at:
                // TODO utilisation de Math.round(...) ? diff(31mai,31mai + 1mois) = 0.96
                write("Math.round(Math.abs(moment("+dequeField.pollFirst()+").diff(");//Math.abs so the date order doesn't matter
                if(dequeField.size()>0){
                    write("moment(");
                    write(dequeField.pollFirst().toString());
                    if((DefaultOperator)dequeOpe.getFirst().getReadable()==with){
                        dequeOpe.pollFirst();
                        manageOperator((DefaultOperator)dequeOpe.pollFirst().getReadable(),null,
                                null,null);
                    }
                }else {
                    manageOperator((DefaultOperator) dequeOpe.pollFirst().getReadable(), null,
                            null, null);
                }
                write("), \'years\')))");
                break;
            case before:
                write("moment("+dequeField.pollFirst().toString()+"" +
                        ").isBefore("+dequeField.pollFirst().toString());
                parenthese_depth++;
                break;
            case before_or_equals:
                write("moment("+dequeField.pollFirst().toString()+"" +
                        ").isSameOrBefore("+dequeField.pollFirst().toString());
                parenthese_depth++;
                break;
            case matches:
                write(".match(/");
                parenthese_depth++;
                use_regexp = 1;
                is_match=1;
                break;
            case contains:
                write(".contains(\'");
                write(dequeValue.pollFirst().toString());
                write("\')");
                break;
            case starts_with:
                write(".match(/^");
                start_with_count++;
                parenthese_depth++;
                use_regexp = 1;
                break;
            case ends_with:
                write(".match(/.*");
                end_with_count++;
                parenthese_depth++;
                use_regexp = 1;
                break;
            case greater_than:
                write(" > ");
                if(dequeValue!=null){
                    write(dequeValue.pollFirst().toString());
                }else if(dequeField!=null){
                    write(dequeField.pollFirst().toString());
                }
                break;
            case greater_or_equals:
                write(" >= ");
                if(dequeValue!=null){
                    write(dequeValue.pollFirst().toString());
                }else if(dequeField!=null){
                    write(dequeField.pollFirst().toString());
                }
                break;
            case is:
                write(" === ");
                break;
            case lesser_than:
                write(" < ");
                if(dequeValue!=null){
                    write(dequeValue.pollFirst().toString());
                }else if(dequeField!=null){
                    write(dequeField.pollFirst().toString());
                }
                break;
            case lesser_or_equals:
                write(" <= ");
                if(dequeValue!=null && dequeValue.size()>0){
                    write(dequeValue.pollFirst().toString());
                }else if(dequeField!=null){
                    write(dequeField.pollFirst().toString());
                }
                break;
            case has_not_size:
                write(".length != ");
                break;
            case has_size:
                write(".length == ");
                break;
            case is_empty:
                write(".length == 0");
                break;
            case is_not_empty:
                write(".length != 0");
                break;
            case length_is:
                if(dequeField!=null){
                    write(dequeField.pollFirst().toString()+".length");
                }else{
                    write(".length");
                }
                break;
            case today:
                write("moment(");
                break;
            case today_plus:
                write("moment().add(");
                break;
            case today_minus:
                write("moment().subtract(");
                break;
            case first_day_of_this_month:
                write("moment().startOf('month'");
                break;
            case first_day_of_this_year:
                write("moment().startOf('year'");
                break;
            case last_day_of_this_month:
                write("moment().endOf('month'");
                break;
            case last_day_of_this_year:
                write("moment().endOf('year'");
                break;
            case first_day_of_month:
                write(").startOf('month'");
                break;
            case first_day_of_next_month:
                write("moment().add(1,'month').startOf('month'");
                break;
            case first_day_of_year:
                write(").startOf('year'");
                break;
            case first_day_of_next_year:
                write("moment().add(1,'year').startOf('year'");
                break;
            case last_day_of_month:
                write(").endOf('month'");
                break;
            case last_day_of_year:
                write(").endOf('year'");
                break;
        }

    }

    /**
     * manage the XOR operator construction
     * @param leftMD left Metadata of the XOR predicate
     * @param rightMD right Metadata of the XOR predicate
     * */
    private void manageXOR(Metadata leftMD, Metadata rightMD){
        write("(!"+leftMD+" && "+rightMD+") || ("+leftMD+" && !"+rightMD+")");
    }

    @Override
    public void startMetadata(StepWhen metadata, int depth){
        write("if(");
    }

    @Override
    public void endMetadata(StepWhen metadata,int depth){
        while(parenthese_depth>0){
            write(")");                 //closing parenthesis for each opened
            parenthese_depth--;
        }
        write("){ true;}else{ false;}\n");
    }

    /**
     * replace in a String object the special characters like |, ., ^, $, (, ), [, ], -, {, }, ?, *, + and /.
     * @param reg the String to format for usage as a regular expression
     * @return the formatted String
     */
    private String formatRegexp(String reg){
        reg=reg.replace("|","\\|");
        reg=reg.replace(".","\\.");
        reg=reg.replace("^","\\^");
        reg=reg.replace("$","\\$");
        reg=reg.replace("(","\\(");
        reg=reg.replace(")","\\)");
        reg=reg.replace("[","\\[");
        reg=reg.replace("]","\\]");
        reg=reg.replace("-","\\-");
        reg=reg.replace("{","\\{");
        reg=reg.replace("}","\\}");
        reg=reg.replace("?","\\?");
        reg=reg.replace("*","\\*");
        reg=reg.replace("+","\\+");
        reg=reg.replace("/","\\/");
        return reg;
    }

    protected void write(String str) {
        try {
            ops.write(str.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
