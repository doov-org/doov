/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.NaryMetadata;
import io.doov.core.dsl.meta.UnaryMetadata;
import io.doov.core.dsl.meta.i18n.ResourceProvider;

public class AstProceduralJSVisitor extends AbstractAstVisitor {

    protected final OutputStream ops;
    protected final ResourceProvider bundle;
    protected final Locale locale;

    public AstProceduralJSVisitor(OutputStream ops, ResourceProvider bundle, Locale locale) {
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
        write(bundle.get(metadata.getOperator(), locale) + metadata.readable());
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata, int depth) {
        String s = metadata.getLeft().readable() + " " + bundle.get(metadata.getOperator(), locale)
                + " " + metadata.getRight().readable();
        s = (depth > 0 ? "(" + s + ")" : s);
        write(s);
    }

    @Override
    public void visitMetadata(LeafMetadata metadata, int depth) {
        write("var arr_test = [];");
        for (Element mtd : metadata.flatten()) {
            Metadata tmp = (Metadata) mtd;
            write("arr_test.push(" + tmp.readable() + ");");
        }
        write("var arrayTrue = [];");
        write("var arrayFalse = [];");
        write("arr_test.forEach(function(value){"
                + "if(" + metadata.readable() + "(value))"
                + "{ arrayTrue.push(value); }"
                + "else{ arrayFalse.push(value); }"
                + "}");

        //test the emptiness of the arrayTrue dictionnary 
        write("if(arrayTrue.length !=0){"
                + "arrayTrue.toString();"
                + "}");
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
        write(")");
    }

    protected void write(String str) {
        try {
            ops.write(str.getBytes("UTF-8"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
