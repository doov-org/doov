/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.i18n.ResourceProvider;

public class AstProceduralJSVisitor extends AbstractAstVisitor {

    protected final OutputStream ops;
    protected final ResourceProvider bundle;
    protected final Locale locale;

    public AstProceduralJSVisitor(OutputStream ops, ResourceProvider bundle, Locale locale) {
        this.ops = ops;
        this.bundle = bundle;
        this.locale = locale;
        write("var moment = require('moment');\n");
        write("moment().format(\"DD-MM-YYYY\");\n");
    }

    @Override
    public void visitMetadata(Metadata metadata, int depth) {
        write(metadata.readable());
    }

    @Override
    public void visitMetadata(UnaryMetadata metadata, int depth) {
        switch (metadata.getOperator().readable()) {
            case "not":
                write("!(" + metadata.readable() + ")");
                break;

            case "as a number":
                write("parseFloat(" + metadata.readable() + ")");
                break;

            case "as a string":
                write("String(" + metadata.readable() + ")");
                break;

            case "is null":
                write("( null == " + metadata.readable() + ")");
                break;

            case "is not null":
                write("(null != " + metadata.readable() + ")");
                break;

            case "when":
                write("if(" + metadata.readable() + "){ return true; }");
                break;

            case "today":
                write("moment()");
                break;

            case "today minus":
                write("moment().subtract(" + metadata.readable() + ",'days')");
                break;

            case "today plus":
                write("moment().add(" + metadata.readable() + ",'days')");
                break;

            case "is empty":
                write("(" + metadata.readable() + ".length == 0 )");
                break;

            case "is not empty":
                write("(" + metadata.readable() + ".length != 0 )");
                break;

            default:
                write("Un-Error"); // will create a syntax error in js
                // if unarymetadata operator unrecognized
                break;
        }

    }

    @Override
    public void endMetadata(UnaryMetadata metadata, int depth) {
        try {
            ops.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata, int depth) {
        switch (metadata.getOperator().readable()) {
            case "and":
                write("(" + metadata.getLeft().readable() + " && " + metadata.getRight() + ")");
                break;

            case "or":
                write("(" + metadata.getLeft().readable() + " || " + metadata.getRight() + ")");
                break;

            case "=":
                write("(" + metadata.getLeft().readable() + " == " + metadata.getRight().readable() + ")");
                break;

            case "!=":
                write("(" + metadata.getLeft().readable() + " != " + metadata.getRight().readable() + ")");
                break;

            case "xor":
                write("((!" + metadata.getLeft() + " && " + metadata.getRight()
                        + ") || (" + metadata.getLeft() + " && !" + metadata.getRight() + "))");
                break;

            case "<":
                write("(" + metadata.getLeft() + " < " + metadata.getRight() + ")");
                break;

            case "<=":
                write("(" + metadata.getLeft() + " <= " + metadata.getRight() + ")");
                break;

            case ">":
                write("(" + metadata.getLeft() + " > " + metadata.getRight() + ")");
                break;

            case ">=":
                write("(" + metadata.getLeft() + " >= " + metadata.getRight() + ")");
                break;

            case "minus":
                write(metadata.getLeft() + ".subtract(" + metadata.getRight() + ",'days')");
                break;

            case "plus":
                write(metadata.getLeft() + ".add(" + metadata.getRight() + ",'days')");
                break;

            case "after":
                write("moment(" + metadata.getLeft() + ").isAfter(" + metadata.getRight() + ")");
                break;

            case "after or equals":
                write("moment(" + metadata.getLeft() + ").isSameOrAfter(" + metadata.getRight() + ")");
                break;

            case "before":
                write("moment(" + metadata.getLeft() + ").isBefore(" + metadata.getRight() + ")");
                break;

            case "before or equals":
                write("moment(" + metadata.getLeft() + ").isSameOrBefore(" + metadata.getRight() + ")");
                break;

            case "has size":
                write("(" + metadata.getLeft() + ".length == " + metadata.getRight() + ")");
                break;

            case "has not size":
                write("(" + metadata.getLeft() + ".length != " + metadata.getRight() + ")");
                break;

            case "age at":
                write("moment.duration(" + metadata.getLeft() + ".diff(" + metadata.getRight() + ")).asYears()");
                break;

            case "starts with":
                write("String(" + metadata.getLeft() + ").match(\"*\"+" + metadata.getRight() + ")");
                break;

            case "ends with":
                write("String(" + metadata.getLeft() + ").match(" + metadata.getRight() + "+\"*\")");
                break;

            case "is":
                write("(" + metadata.getLeft() + " === " + metadata.getRight() + ")");
                break;

            default:
                write("Bin-Error");// will create a syntax error in js
                // if binarymetadata operator unrecognized
                break;
        }
    }

    @Override
    public void endMetadata(BinaryMetadata metadata, int depth) {
        try {
            ops.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitMetadata(LeafMetadata metadata, int depth) {


        System.out.println("----");
        metadata.stream().peek(System.out::print).forEach(element -> {
            switch (element.getType()) {

                case FIELD:
                    System.out.println("         FIELD");
                    break;
                case OPERATOR:
                    System.out.println("         OPERATOR");
                    break;
                case VALUE:
                    System.out.println("         VALUE");
                    break;
                case STRING_VALUE:
                    System.out.println("         STRING_VALUE");
                    break;
                case PARENTHESIS_LEFT:
                    System.out.println("         PARENTHESIS_LEFT");
                    break;
                case PARENTHESIS_RIGHT:
                    System.out.println("         PARENTHESIS_RIGHT");
                    break;
                case TEMPORAL_UNIT:
                    System.out.println("         TEMPORAL_UNIT");
                    break;
                case UNKNOWN:
                    System.out.println("         UNKNOWN");
                    break;
            }
        });
        System.out.println("----");


        //        switch (metadata.type()){
        //            case FIELD_PREDICATE_MATCH_ANY:
        //                List<Metadata> children = metadata.children();
        //                write("match("+", "+")");
        //                break;
        //            default:
        //                write("Leaf-Error");// will create a syntax error in js
        //                 if leafmetadata operator unrecognized
        //                break;
        //        }
    }

    @Override
    public void endMetadata(LeafMetadata metadata, int depth) {
        try {
            ops.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitMetadata(NaryMetadata metadata, int depth) {
        List<Metadata> children = metadata.children();
        write("function(){");
        write("var array-test = [");
        for (Metadata mtd : children) {
            write(mtd.readable());
            if (children.indexOf(mtd) != children.size() - 1) {
                write(", ");
            }
        }
        write("];\n");
        switch (metadata.getOperator().readable()) {
            case "count":
                write("var nb-true = 0;" +
                        "array-test.forEach(function(item){" +
                        "if(item){" +
                        "nb-true++;" +
                        "}" +
                        "});" +
                        "return nb-true;");
                break;
            case "match all":
                write("var test = 0 ;");
                write("array-test.forEach(function(item){" +
                        "if(!item){" +
                        "test = 1;}" +
                        "});");
                write("if(test != 0 ){" +
                        "return false;}" +
                        "return true;");
                break;
            case "match none":
                write("var test = 0 ;");
                write("array-test.forEach(function(item){" +
                        "if(item){" +
                        "test = 1;}" +
                        "});");
                write("if(test != 0 ){" +
                        "return false;}" +
                        "return true;");
                break;
            case "match any":
                write("var array-test = [");
                for (Metadata mtd : children) {
                    write(mtd.readable());
                    if (children.indexOf(mtd) != children.size() - 1) {
                        write(", ");
                    }
                }
                write("];");
                write("array-test.forEach(function(item){" +
                        "if(item){" +
                        "return true;}" +
                        "});");
                break;
            case "sum":
                write("array-test.reduce(function(acc,item)" +
                        "{ return acc + item;" +
                        "});");

                break;

            case "min":
                write("Math.min(array-test);");
                break;
            default:
                write("Nary-Error");// will create a syntax error in js
                // if narymetadata operator unrecognized
                break;
        }
        write("}\n");
    }

    @Override
    public void endMetadata(NaryMetadata metadata, int depth) {
        try {
            write("\n");
            ops.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitMetadata(ValidationRule metadata, int depth) {
    }

    @Override
    public void visitMetadata(StepWhen metadata, int depth) {

        write("if(");
        visit(metadata.stepCondition().getMetadata(), depth + 1);
        write("){ return true; }\n");
    }

    @Override
    public void visitMetadata(StepCondition metadata, int depth) {
    }

    protected void write(String str) {
        try {
            ops.write(str.getBytes("UTF-8"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
