/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.i18n.ResourceProvider;

public class AstProceduralJSVisitor extends AbstractAstVisitor {

    protected final OutputStream ops;
    protected final ResourceProvider bundle;
    protected final Locale locale;
    private int parenthese_depth = 0;
    private int start_with_count = 0;
    private int nary_args_count = 0 ;

    public AstProceduralJSVisitor(OutputStream ops, ResourceProvider bundle, Locale locale) {
        this.ops = ops;
        this.bundle = bundle;
        this.locale = locale;
        write("var moment = require('moment');\n");
        write("moment().format(\"DD-MM-YYYY\");\n");
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
            write("!");
        }
        if(metadata.getOperator()==count){
            write("function(){ var count=0; var array-test = [");
        }
    }


    @Override
    public void visitMetadata(NaryMetadata metadata,int depth){
        nary_args_count++;
        if(nary_args_count!=metadata.children().size()) {
            switch((DefaultOperator)metadata.getOperator()){
                case match_any:
                    write(" || ");
                    break;
                case match_all:
                    write(" && ");
                    break;
                case match_none:
                    write(" && !");
                    break;
                case count:
                    write(", ");
                    break;
            }
        }
    }

    @Override
    public void endMetadata(NaryMetadata metadata,int depth){
        nary_args_count = 0 ;
        if(metadata.getOperator()==count){
            write("]; array-test.forEach(function(item){" +
                    "if(item){count++;}} return count;}");
        }

    }

    @Override
    public void visitMetadata(LeafMetadata metadata, int depth) {
        ArrayList<Element> date_field = new ArrayList<>();
        final int[] is_age_at = { 0 };
        metadata.stream().forEach(element -> {
            switch (element.getType()) {
                case FIELD:
                    write(element.toString());
                    break;
                case OPERATOR:
                    manageOperator((DefaultOperator)element.getReadable());
                    break;
                case VALUE:
                    write(element.toString());
                    break;
                case STRING_VALUE:
                    write("\""+element+"\"");
                    break;
                case PARENTHESIS_LEFT:
                    write(element.toString());
                    break;
                case PARENTHESIS_RIGHT:
                    write(element.toString());
                    break;
                case TEMPORAL_UNIT:
                    write(element.toString());
                    break;
                case UNKNOWN:
                    write("Unknown "+element.toString());
                    break;
            }
        });
        if(start_with_count>0){
            write("+\"*\"");
            start_with_count--;
        }
        if(parenthese_depth>0) {
            write(")");
            parenthese_depth--;
        }
    }

    public void manageOperator(DefaultOperator element){

        switch(element){
            case rule:
                break;
            case validate:
                break;
            case empty:
                break;
            case sum:
                break;
            case min:
                break;
            case not:
                write("!");
                break;
            case always_true:
                break;
            case always_false:
                break;
            case times:
                break;
            case equals:
                write(" == ");
                break;
            case not_equals:
                write(" != ");
                break;
            case is_null:
                write(" == null ");
                break;
            case is_not_null:
                write(" != null ");
                break;
            case as_a_number:
                break;
            case as_string:
                break;
            case as:
                break;
            case with:
                break;
            case minus:
                break;
            case plus:
                break;
            case after:
                break;
            case after_or_equals:
                break;
            case age_at:

                break;
            case before:
                break;
            case before_or_equals:
                break;
            case matches:
                write(".match(");
                parenthese_depth++;
                break;
            case contains:
                break;
            case starts_with:
                write(".match(\"^\"+");
                start_with_count++;
                parenthese_depth++;
                break;
            case ends_with:
                write(".match(\"*\"+");
                parenthese_depth++;
                break;
            case greater_than:
                write(" > ");
                break;
            case greater_or_equals:
                write(" >= ");
                break;
            case xor:
                break;
            case is:
                write(" === ");
                break;
            case lesser_than:
                write(" < ");
                break;
            case lesser_or_equals:
                write(" <= ");
                break;
            case has_not_size:
                break;
            case has_size:
                break;
            case is_empty:
                break;
            case is_not_empty:
                break;
            case length_is:
                break;
            case today:
                write("moment()");
                break;
            case today_plus:
                break;
            case today_minus:
                break;
            case first_day_of_this_month:
                break;
            case first_day_of_this_year:
                break;
            case last_day_of_this_month:
                break;
            case last_day_of_this_year:
                break;
            case first_day_of_month:
                break;
            case first_day_of_next_month:
                break;
            case first_day_of_year:
                break;
            case first_day_of_next_year:
                break;
            case last_day_of_month:
                break;
            case last_day_of_year:
                break;

        }

    }

    @Override
    public void startMetadata(StepWhen metadata, int depth){
        write("function when(){if(");
    }

    @Override
    public void endMetadata(StepWhen metadata,int depth){
        write("){return true;}return false; }\n");
    }

    protected void write(String str) {
        try {
            ops.write(str.getBytes("UTF-8"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
