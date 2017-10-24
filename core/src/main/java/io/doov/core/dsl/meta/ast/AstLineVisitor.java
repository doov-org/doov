package io.doov.core.dsl.meta.ast;

import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.NaryMetadata;

public class AstLineVisitor extends AstTextVisitor {

    public AstLineVisitor(StringBuilder stringBuilder) {
        super(stringBuilder);
    }

    @Override
    protected String formatCurrentIndent() {
        return "";
    }

    @Override
    protected String formatNewLine() {
        return " ";
    }

    @Override
    public void startMetadata(NaryMetadata metadata) {
        super.startMetadata(metadata);
        sb.append("[");
    }

    @Override
    public void visitMetadata(NaryMetadata metadata) {
        super.visitMetadata(metadata);
        sb.deleteCharAt(sb.length() - 1);
        sb.append(", ");
    }

    @Override
    protected void endMetadata(NaryMetadata metadata) {
        super.endMetadata(metadata);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("] ");
    }

    @Override
    protected void startMetadata(BinaryMetadata metadata) {
        super.startMetadata(metadata);
        sb.append("(");
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata) {
        sb.append(formatOperator(metadata));
        sb.append(formatNewLine());
    }

    @Override
    protected void endMetadata(BinaryMetadata metadata) {
        super.endMetadata(metadata);
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") ");
    }

    @Override
    protected void endMetadata(ValidationRule metadata) {
        super.endMetadata(metadata);
        sb.append("\n");
    }

}