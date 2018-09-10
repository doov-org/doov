/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

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
        write("var moment = require('moment');");
        write("moment().format(\"DD-MM-YYYY\");");
    }

    @Override
    public void visitMetadata(Metadata metadata, int depth) {
        write(metadata.readable());
    }

    @Override
    public void visitMetadata(UnaryMetadata metadata, int depth) {
        switch(metadata.getOperator().readable()){
            case "not":
                write("!"+metadata.readable());
                break;
            case "as a number":
                write("parseInt("+metadata.readable()+")");
                break;
            case "as a string":
                write("String("+metadata.readable()+")");
                break;
            case "is null":
                write("null == "+metadata.readable());
                break;
            case "is not null":
                write("null != "+metadata.readable());
                break;
            case "when":
                write("if("+metadata.readable()+"){ return true; }");
                break;
            case "today":
                write("var now = moment();");
                break;
            case "today minus" :
                write("var now = moment();");
                write("now.subtract("+metadata.readable()+",'days');");
                break;
            case "today plus":
                write("var now = moment();");
                write("now.add("+metadata.readable()+",'days');");
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
        switch(metadata.getOperator().readable()){
            case "and" :
                write(metadata.getLeft().readable()+" && "+metadata.getRight());
                break;
            case "or" :
                write(metadata.getLeft().readable()+" || "+metadata.getRight());
                break;
            case "=":
                write(metadata.getLeft().readable()+" == "+metadata.getRight().readable());
                break;
            case "!=":
                write(metadata.getLeft().readable()+" != "+metadata.getRight().readable());
                break;
            case "xor":
                write("(!"+metadata.getLeft()+" && "+metadata.getRight()
                    +") || ("+metadata.getLeft()+" && !"+metadata.getRight()+")");
                break;
            case "<":
                write(metadata.getLeft()+" < "+metadata.getRight());
                break;
            case "<=":
                write(metadata.getLeft()+" <= "+metadata.getRight());
                break;
            case ">":
                write(metadata.getLeft()+" > "+metadata.getRight());
                break;
            case ">=":
                write(metadata.getLeft()+" >= "+metadata.getRight());
                break;
            case "matches":
                write("match("+metadata.getLeft()+", "+metadata.getRight()+");");
                break;

            case "minus" :
                write(metadata.getLeft()+".subtract("+metadata.getRight()+",'days');");
                break;
            case "plus" :
                write(metadata.getLeft()+".add("+metadata.getRight()+",'days');");
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
        switch (metadata.readable()){
            default:
                write("Leaf-Error");// will create a syntax error in js
                // if leafmetadata operator unrecognized
                break;
        }
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
        write("var array-test = [");
        for (Metadata mtd: children) {
            write(mtd.readable());
            if(children.indexOf(mtd)!=children.size()-1){
                write(", ");
            }
        }
        write("];");
        switch(metadata.getOperator().readable()){
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
                for (Metadata mtd: children) {
                    write(mtd.readable());
                    if(children.indexOf(mtd)!=children.size()-1){
                        write(", ");
                    }
                }
                write("];");
                write("array-test.forEach(function(item){" +
                        "if(item){" +
                        "return true;}" +
                        "});");
                break;
            case "sum" :
                write("array-test.reduce(function(acc,item)" +
                        "{ return acc + item;" +
                        "});");

                break;

            case "min" :
                write("Math.min(array-test);");
                break;
            default:
                write("Nary-Error");// will create a syntax error in js
                // if narymetadata operator unrecognized
                break;
        }
    }

    @Override
    public void endMetadata(NaryMetadata metadata, int depth) {
        try {
            ops.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void write(String str) {
        try {
            ops.write(str.getBytes("UTF-8"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
