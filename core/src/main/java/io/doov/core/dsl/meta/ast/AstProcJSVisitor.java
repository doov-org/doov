/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.i18n.ResourceProvider;

public class AstProcJSVisitor extends AbstractAstVisitor {

    protected final OutputStream ops;
    protected final ResourceProvider bundle;
    protected final Locale locale;

    public AstProcJSVisitor(OutputStream ops, ResourceProvider bundle, Locale locale) {
        this.ops = ops;
        this.bundle = bundle;
        this.locale = locale;
    }

    @Override
    public void visitMetadata(Metadata metadata, int depth) {
        write(metadata.readable());
    }

    @Override
    public void visitMetadata(UnaryMetadata metadata, int depth) {
        write( bundle.get(metadata.getOperator(), locale) + " " + metadata.readable());
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
        String s = metadata.getLeft().readable() + " " + bundle.get(metadata.getOperator(), locale)
                + " " + metadata.getRight().readable();
        s = (depth > 0 ? " ( " + s + " ) " : s);
        write(s);
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
        write("var arr_test = [];");

        for (Metadata mtd : metadata.children()) {
            write("arr_test.push(" + mtd.readable() + ");");
        }
        write("var arrayTrue = [];");
        write("var arrayFalse = [];");
        write("arr_test.forEach(function(value){"
                + "if(" + metadata.readable() + "(value))"
                + "{ arrayTrue.push(value); }"
                + "else{ arrayFalse.push(value); }"
                + "}");

        //test the emptiness of the arrayTrue dictionnary
        write("if(arrayTrue.length != 0){"
                + "arrayTrue.toString();"
                + "}");
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
        write("var final_predicate = " + metadata.getOperator().readable() + "(");
        List<Metadata> children_list = metadata.children();
        for (Metadata mtd : children_list) {
            write(mtd.readable());
            if (children_list.indexOf(mtd) != children_list.size() - 1) {
                write(", ");
            }
        }
        write(");");
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
