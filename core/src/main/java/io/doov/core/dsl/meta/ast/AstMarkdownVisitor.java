package io.doov.core.dsl.meta.ast;

import io.doov.core.dsl.meta.Readable;

public class AstMarkdownVisitor extends AstTextVisitor {

    public AstMarkdownVisitor(StringBuilder stringBuilder) {
        super(stringBuilder);
    }

    @Override
    protected String formatCurrentIndent() {
        return super.formatCurrentIndent() + "* ";
    }

    @Override
    protected String formatField(Readable field) {
        return "*" + field.readable() + "*";
    }

    @Override
    protected String formatOperator(Readable operator) {
        return "**" + operator.readable() + "**";
    }

}