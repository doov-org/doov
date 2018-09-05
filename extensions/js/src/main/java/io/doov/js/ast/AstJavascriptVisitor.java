/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.js.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.ast.AbstractAstVisitor;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;
import io.doov.core.dsl.meta.predicate.NaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.UnaryPredicateMetadata;
import org.apache.commons.lang3.StringUtils;

public class AstJavascriptVisitor extends AbstractAstVisitor {

    private final OutputStream ops;
    protected final ResourceProvider bundle;
    protected final Locale locale;

    private int parenthese_depth = 0;   // define the number of parenthesis to close before ending the rule rewriting
    private int start_with_count = 0;   // define the number of 'start_with' rule used for closing parenthesis purpose
    private int end_with_count = 0;     // define the number of 'start_with' rule used for closing parenthesis purpose
    private int use_regexp = 0;         // boolean as an int to know if we are in a regexp for closing parenthesis purpose
    private int is_match = 0;             // boolean as an int to know if we are in a matching rule for closing parenthesis purpose

    public AstJavascriptVisitor(OutputStream ops, ResourceProvider bundle, Locale locale) {
        this.ops = ops;
        this.bundle = bundle;
        this.locale = locale;
    }

    @Override
    public void startBinary(BinaryPredicateMetadata metadata, int depth) {
        write("(");
    }

    @Override
    public void afterChildBinary(BinaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
        if (hasNext) {
            switch ((DefaultOperator) metadata.getOperator()) {
                case and:
                    write(" && ");
                    break;
                case or:
                    write(" || ");
                    break;
                case xor:
                    manageXOR(metadata.getLeft(), metadata.getRight());
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
    }

    @Override
    public void endBinary(BinaryPredicateMetadata metadata, int depth) {
        write(")");
    }

    @Override
    public void startNary(NaryPredicateMetadata metadata, int depth) {
        if (metadata.getOperator() == match_none) {
            write("!");                             // for predicate [a,b,c] will translate as (!a && !b && !c)
        }
        if (metadata.getOperator() == count || metadata.getOperator() == sum) {
            write("[");                             // opening a list to use count or sum on
        }
        if (metadata.getOperator() == min) {
            write("Math.min.apply(null,[");         // using JS Math module to apply min on a list, opening the list
        }
    }

    @Override
    public void afterChildNary(NaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
        if (hasNext) {
            switch ((DefaultOperator) metadata.getOperator()) {
                case match_any:
                    write(" || ");                       // using 'or' operator to match any of the predicate given
                    break;
                case match_all:
                    write(" && ");                       // using 'and' operator for match all
                    break;
                case match_none:
                    write(" && !");                      // 'and not' for match none
                    break;
                case min:
                case sum:
                case count:
                    write(", ");                         // separating the list values
                    break;
            }
        }
    }

    @Override
    public void endNary(NaryPredicateMetadata metadata, int depth) {
        if (metadata.getOperator() == count) {
            write("].reduce(function(acc,val){if(val){return acc+1;}return acc;},0)");
        }                                                       // using reduce method to count with an accumulator
        if (metadata.getOperator() == min) {
            write("])");                                    // closing the value list
        }
        if (metadata.getOperator() == sum) {
            write("].reduce(function(acc,val){ return acc+val;},0)");
        }                                                       // using reduce method to sum the value
    }

    @Override
    public void startLeaf(LeafPredicateMetadata<?> metadata, int depth) {
        ArrayDeque<Element> stack = new ArrayDeque<>();    //using arrayDeque to store the fields
        metadata.elements().forEach(element -> {
            switch (element.getType()) {
                case OPERATOR:
                    if (element.getReadable() == age_at
                            || element.getReadable() == as_a_number                       // checking for special cases
                            || element.getReadable() == as_string || element.getReadable() == before
                            || element.getReadable() == before_or_equals || element.getReadable() == after
                            || element.getReadable() == after_or_equals) {
                        stack.addFirst(element);
                    } else {
                        stack.add(element);
                    }
                    break;
                case STRING_VALUE:
                case FIELD:
                case VALUE:
                case TEMPORAL_UNIT:
                    stack.add(element);
                    break;
                case UNKNOWN:
                    write("/* Unknown " + element.toString() + "*/");
                    break;
                case PARENTHESIS_LEFT:
                case PARENTHESIS_RIGHT:
                    write("/*" + element.toString() + "*/");
                    break;
            }
        });
        manageStack(stack);
        while (parenthese_depth > 0) {
            write(")");
            parenthese_depth--;
        }
    }

    @Override
    public void beforeChildUnary(UnaryPredicateMetadata metadata, Metadata child, int depth) {
        manageOperator((DefaultOperator) metadata.getOperator(), null);

    }

    @Override
    public void endUnary(UnaryPredicateMetadata metadata, int depth) {
        write(")");
    }


    /**
     * This function manage the different parameters of the predicate
     *
     * @param stack A deque of the predicate parameters
     */
    private void manageStack(ArrayDeque<Element> stack) {
        while (stack.size() > 0) {
            Element e = stack.pollFirst();
            switch (e.getType()) {
                case FIELD:
                    write(e.toString());
                    break;
                case OPERATOR:
                    manageOperator((DefaultOperator) e.getReadable(), stack);
                    break;
                case VALUE:
                    if (StringUtils.isNumeric(e.toString())) {
                        write(e.toString());
                    } else {
                        write("\'" + e.toString() + "\'");
                    }
                    break;
                case STRING_VALUE:
                    if (use_regexp == 1) {
                        String tmp = e.toString();
                        if (is_match == 1) {
                            is_match = 0;
                        } else {
                            tmp = formatRegexp(tmp);
                        }
                        write(tmp);
                        if (start_with_count > 0) {
                            write(".*");
                            start_with_count--;
                        } else if (end_with_count > 0) {
                            write("$");
                            end_with_count--;
                        }
                        write("/");
                        use_regexp = 0;
                    } else {
                        write("\'" + e.toString() + "\'");
                    }
                    break;
                case PARENTHESIS_LEFT:
                case PARENTHESIS_RIGHT:
                case UNKNOWN:
                    break;
            }
        }
    }

    /**
     * this method will write the javascript translation for the operator of the predicate
     *
     * @param element the default operator of the LeafMetadata
     * @param stack   the deque of the parameters not translated yet to Javascript predicate
     */
    private void manageOperator(DefaultOperator element, ArrayDeque<Element> stack) {
        ArrayDeque<Element> stackTmp = new ArrayDeque<>();
        switch (element) {
            case rule:
            case validate:
            case empty:
            case as:
                break;
            case with:
                manageStack(stack);
                break;
            case as_a_number:
                if (stack.size() > 0) {
                    write("parseInt(");
                    write(stack.pollFirst().toString());
                    write(")");
                }
                break;
            case as_string:
                if (stack.size() > 0) {
                    write("String(" + stack.pollFirst().toString() + ")");
                }
                break;
            case not:
                write("!(");
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
                stackTmp.add(stack.pollFirst());
                manageStack(stackTmp);
                break;
            case not_equals:
                write(" !== ");
                if (stack != null) {
                    write(stack.pollFirst().toString());
                } else if (stack != null) {
                    write(stack.pollFirst().toString());
                }
                break;
            case is_null:
                write(" === ( null || undefined || \"\" ) ");
                break;
            case is_not_null:
                write(" !== ( null || undefined || \"\" ) ");
                break;
            case minus:
                write(".subtract(" + stack.pollFirst().toString() + "," +
                        "\'" + stack.pollFirst().toString() + "\')");
                break;
            case plus:
                write(".add(" + stack.pollFirst() + "," +
                        "\'" + stack.pollFirst().toString() + "\')");
                break;
            case after:
                write("moment(" + stack.pollFirst().toString() + "" +
                        ").isAfter(moment(" + stack.pollFirst().toString() + ")");
                parenthese_depth++;
                break;
            case after_or_equals:
                write("moment(" + stack.pollFirst().toString() + "" +
                        ").isSameOrAfter(moment(" + stack.pollFirst().toString() + ")");
                parenthese_depth++;
                break;
            case age_at:
                write("Math.round(Math.abs(moment(");               // using Math.round(...)
                stackTmp.add(stack.pollFirst());                        // ex : diff(31may,31may + 1month) = 0.96
                manageStack(stackTmp);
                formatAgeAtOperator(stack);
                write(").diff(");                                   //Math.abs so the date order doesn't matter
                write("moment(");
                stackTmp.add(stack.pollFirst());
                manageStack(stackTmp);
                write(")");
                formatAgeAtOperator(stack);
                write(", \'years\')))");
                break;
            case before:
                write("moment(" + stack.pollFirst().toString() + "" +
                        ").isBefore(" + stack.pollFirst().toString());
                parenthese_depth++;
                break;
            case before_or_equals:
                write("moment(" + stack.pollFirst().toString() + "" +
                        ").isSameOrBefore(" + stack.pollFirst().toString());
                parenthese_depth++;
                break;
            case matches:
                write(".match(/");
                parenthese_depth++;
                use_regexp = 1;
                is_match = 1;
                break;
            case contains:
                write(".contains(\'");
                write(stack.pollFirst().toString());
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
                if (stack != null && stack.size() > 0) {
                    write(stack.pollFirst().toString());
                }
                break;
            case greater_or_equals:
                write(" >= ");
                if (stack != null && stack.size() > 0) {
                    write(stack.pollFirst().toString());
                }
                break;
            case is:
                write(" === ");
                break;
            case lesser_than:
                write(" < ");
                if (stack != null && stack.size() > 0) {
                    write(stack.pollFirst().toString());
                }
                break;
            case lesser_or_equals:
                write(" <= ");
                if (stack != null && stack.size() > 0) {
                    write(stack.pollFirst().toString());
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
                write(".length");
                break;
            case today:
                write("moment()");
                break;
            case today_plus:
                write("moment().add(");
                break;
            case today_minus:
                write("moment().subtract(");
                break;
            case first_day_of_this_month:
                write("moment().startOf('month')");
                break;
            case first_day_of_this_year:
                write("moment().startOf('year')");
                break;
            case last_day_of_this_month:
                write("moment().endOf('month')");
                break;
            case last_day_of_this_year:
                write("moment().endOf('year')");
                break;
            case first_day_of_month:
                write(".startOf('month')");
                break;
            case first_day_of_next_month:
                write("moment().add(1,'month').startOf('month')");
                break;
            case first_day_of_year:
                write(".startOf('year')");
                break;
            case first_day_of_next_year:
                write("moment().add(1,'year').startOf('year')");
                break;
            case last_day_of_month:
                write(".endOf('month')");
                break;
            case last_day_of_year:
                write(".endOf('year')");
                break;
        }

    }


    /**
     * age_at operator specials cases for the parameter in moment.js
     *
     * @param stack the deque of the parameters not translated yet to Javascript predicate
     */
    private void formatAgeAtOperator(ArrayDeque<Element> stack) {
        ArrayDeque<Element> stackTmp = new ArrayDeque<>();
        if (stack.getFirst().getReadable() == with || stack.getFirst().getReadable() == plus
                || stack.getFirst().getReadable() == minus) {
            if (stack.getFirst().getReadable() == with) {
                stack.pollFirst();
                stackTmp.add(stack.pollFirst());
                manageStack(stackTmp);
            } else {                                      // working on plus and minus operators
                Element ope = stack.pollFirst();        // need the three first elements of the stack to manage
                Element duration = stack.pollFirst();   // these operators
                Element unit = stack.pollFirst();
                stackTmp.add(ope);
                stackTmp.add(duration);
                stackTmp.add(unit);
                manageStack(stackTmp);
            }
        }
    }


    /**
     * XOR operator construction and writing
     *
     * @param leftMD  left Metadata of the XOR predicate
     * @param rightMD right Metadata of the XOR predicate
     */
    private void manageXOR(Metadata leftMD, Metadata rightMD) {
        write("(!" + leftMD + " && " + rightMD + ") || (" + leftMD + " && !" + rightMD + ")");
    }

    @Override
    public void startWhen(WhenMetadata metadata, int depth) {
        write("if(");
    }

    @Override
    public void endWhen(WhenMetadata metadata, int depth) {
        while (parenthese_depth > 0) {
            write(")");                 //closing parenthesis
            parenthese_depth--;
        }
        write("){ true;}else{ false;}\n");
    }

    /**
     * replace in a String object the special characters |, ., ^, $, (, ), [, ], -, {, }, ?, *, + and /.
     *
     * @param reg the String to format for usage as a regular expression
     * @return the formatted String
     */
    private String formatRegexp(String reg) {
        reg = reg.replace("|", "\\|");
        reg = reg.replace(".", "\\.");
        reg = reg.replace("^", "\\^");
        reg = reg.replace("$", "\\$");
        reg = reg.replace("(", "\\(");
        reg = reg.replace(")", "\\)");
        reg = reg.replace("[", "\\[");
        reg = reg.replace("]", "\\]");
        reg = reg.replace("-", "\\-");
        reg = reg.replace("{", "\\{");
        reg = reg.replace("}", "\\}");
        reg = reg.replace("?", "\\?");
        reg = reg.replace("*", "\\*");
        reg = reg.replace("+", "\\+");
        reg = reg.replace("/", "\\/");
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
