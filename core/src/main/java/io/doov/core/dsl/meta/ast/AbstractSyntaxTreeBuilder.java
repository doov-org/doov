package io.doov.core.dsl.meta.ast;

import static java.util.stream.Collectors.joining;

import java.util.stream.IntStream;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.Readable;

public abstract class AbstractSyntaxTreeBuilder extends AbstractSyntaxTree {

    private final StringBuilder sb;

    private boolean fieldIndent = true;

    AbstractSyntaxTreeBuilder(StringBuilder stringBuilder) {
        sb = stringBuilder;
    }

    @Override
    public void visit(Metadata metadata) {
    }

    @Override
    public void visit(FieldMetadata fieldMetadata) {
        if (Element.WHEN.equals(peek())) {
            sb.append(formatIndent());
        }

        if (Element.BINARY.equals(peek())) {
            if (fieldIndent) {
                sb.append(formatIndent());
            }
        }

        if (Element.NARY.equals(peek())) {
            sb.append(formatIndent());
        }

        sb.append(formatField(fieldMetadata.getField()))
                        .append(" ")
                        .append(formatOperator(fieldMetadata.getOperator()))
                        .append(" ")
                        .append(formatValue(fieldMetadata.getValue()));

        if (Element.NARY.equals(peek())) {
            sb.append("\n");
        }
    }

    @Override
    public void visit(UnaryMetadata unaryMetadata) {
        sb.append(formatLogical(unaryMetadata.getOperator()));
    }

    @Override
    public void start(BinaryMetadata binaryMetadata) {
        if (Element.NARY.equals(peek())) {
            sb.append(formatIndent());
            fieldIndent = false;
        }
        super.start(binaryMetadata);
    }

    @Override
    public void visit(BinaryMetadata binaryMetadata) {
        sb.append(" ").append(formatLogical(binaryMetadata.getOperator()));
        sb.append("\n");
        fieldIndent = true;
    }

    @Override
    public void end(BinaryMetadata binaryMetadata) {
        super.end(binaryMetadata);
        if (Element.WHEN.equals(peek())) {
            sb.append("\n");
        }
    }

    @Override
    public void start(NaryMetadata naryMetadata) {
        sb.append(formatIndent());
        super.start(naryMetadata);
    }

    @Override
    public void visit(NaryMetadata naryMetadata) {
        sb.append(formatLogical(naryMetadata.getOperator()));
        sb.append("\n");
    }

    @Override
    public void end(NaryMetadata naryMetadata) {
        super.end(naryMetadata);
        if (Element.WHEN.equals(peek())) {
            sb.append("\n");
        }
    }

    @Override
    public void start(ValidationRule validationRule) {
        super.start(validationRule);
    }

    @Override
    public void visit(ValidationRule validationRule) {
        sb.append(formatValidateWithMessage());
        sb.append(formatIndent());
        sb.append(formatValidationMessage(validationRule.getMessage()));
    }

    @Override
    public void end(ValidationRule validationRule) {
        sb.append("\n");
        super.end(validationRule);
    }

    @Override
    public void start(StepWhen stepWhen) {
        super.start(stepWhen);
    }

    @Override
    public void visit(StepWhen stepWhen) {
        sb.append(formatWhen());
    }

    @Override
    public void end(StepWhen stepWhen) {
        super.end(stepWhen);
        sb.append("\n");
    }

    @Override
    public void visit(StepCondition stepCondition) {
    }

    public String formatLogical(String binary) {
        return binary;
    }

    public String formatIndent() {
        return formatIndent(getCurrentIndentSize());
    }

    public String formatIndent(int size) {
        return IntStream.range(0, size).mapToObj(i -> " ").collect(joining(""));
    }

    public String formatField(Readable field) {
        return field.readable();
    }

    public String formatOperator(Readable operator) {
        return operator.readable();
    }

    public String formatValue(Readable value) {
        return value.readable();
    }

    public String formatWhen() {
        return "When\n";
    }

    public String formatValidateWithMessage() {
        return "validate with message\n";
    }

    public String formatValidationMessage(String message) {
        return message == null ? "empty" : message;
    }

}