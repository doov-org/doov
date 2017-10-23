package io.doov.core.dsl.meta.ast;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class SyntaxTreeFullBuilder extends AbstractSyntaxTree {

    private static final int INDENT_SIZE = 4;

    public SyntaxTreeFullBuilder(StringBuilder stringBuilder) {
        super(stringBuilder);
    }

    @Override
    public void visitMetadata(Metadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit Metadata " + metadata);
        sb.append("\n");
    }

    @Override
    public void visitMetadata(FieldMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit FieldMetadata " + metadata + " = " + metadata.readable());
        sb.append("\n");
    }

    @Override
    public void visitMetadata(UnaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit UnaryMetadata " + metadata + " = " + metadata.getOperator());
        sb.append("\n");
    }

    @Override
    public void startMetadata(BinaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("start BinaryMetadata " + metadata + " = " + "-");
        sb.append("\n");
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit BinaryMetadata " + metadata + " = " + metadata.getOperator());
        sb.append("\n");
    }

    @Override
    public void endMetadata(BinaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("end BinaryMetadata " + metadata + " = " + "-");
        sb.append("\n");
    }

    @Override
    public void startMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("start NaryMetadata " + metadata + " = " + metadata.getOperator());
        sb.append("\n");
    }

    @Override
    public void visitMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit NaryMetadata " + metadata + " = " + "-");
        sb.append("\n");
    }

    @Override
    public void endMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("end NaryMetadata " + metadata + " = " + "-");
        sb.append("\n");
    }

    @Override
    public void startMetadata(ValidationRule metadata) {
        sb.append(formatCurrentIndent());
        sb.append("start ValidationRule " + metadata + " = " + "Rule");
        sb.append("\n");
    }

    @Override
    public void visitMetadata(ValidationRule metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit ValidationRule " + metadata + " = " + "validate with message");
        sb.append("\n");
    }

    @Override
    public void endMetadata(ValidationRule metadata) {
        sb.append(formatCurrentIndent());
        sb.append("end ValidationRule " + metadata + " = " + metadata.getMessage());
        sb.append("\n");
    }

    @Override
    public void startMetadata(StepWhen metadata) {
        sb.append(formatCurrentIndent());
        sb.append("start StepWhen " + metadata + " = " + "when");
        sb.append("\n");
    }

    @Override
    public void visitMetadata(StepWhen metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit StepWhen " + metadata + " = " + "-");
        sb.append("\n");
    }

    @Override
    public void endMetadata(StepWhen metadata) {
        sb.append(formatCurrentIndent());
        sb.append("end StepWhen " + metadata + " = " + "-");
        sb.append("\n");
    }

    @Override
    public void visitMetadata(StepCondition metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit StepCondition " + metadata + " = " + "-");
        sb.append("\n");
    }

    @Override
    protected int getIndentSize() {
        return INDENT_SIZE;
    }

}
