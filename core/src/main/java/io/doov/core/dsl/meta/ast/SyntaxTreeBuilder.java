package io.doov.core.dsl.meta.ast;

import static java.util.stream.Collectors.joining;

import java.util.*;
import java.util.stream.IntStream;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.Readable;

public abstract class SyntaxTreeBuilder implements MetadataVisitor {

    private static final int INDENT_SIZE = 2;

    private enum Last {
        UNARY, BINARY, NARY
    }

    final StringBuilder sb;

    private final Deque<Last> stack = new ArrayDeque<>();

    private boolean fieldIndent = true;

    SyntaxTreeBuilder(StringBuilder stringBuilder) {
        sb = stringBuilder;
    }

    private int getStackSize() {
        return new HashSet<>(stack).size();
    }

    @Override
    public void visit(Metadata metadata) {
    }

    @Override
    public void visit(FieldMetadata fieldMetadata) {
        if (Last.NARY.equals(stack.peek())) {
            sb.append(formatIndent(getCurrentIndentSize()));
        }

        if (Last.BINARY.equals(stack.peek())) {
            if (fieldIndent) {
                sb.append(formatIndent(getCurrentIndentSize()));
            }
        }

        sb.append(formatField(fieldMetadata.getField()))
                        .append(" ")
                        .append(formatOperator(fieldMetadata.getOperator()))
                        .append(" ")
                        .append(formatValue(fieldMetadata.getValue()));

        if (Last.NARY.equals(stack.peek())) {
            sb.append("\n");
        }
    }

    @Override
    public void visit(UnaryMetadata unaryMetadata) {
        sb.append(formatLogical(unaryMetadata.getOperator()));
    }

    @Override
    public void start(BinaryMetadata binaryMetadata) {
        if (Last.NARY.equals(stack.peek())) {
            sb.append(formatIndent(getCurrentIndentSize()));
            fieldIndent = false;
        }
        stack.push(Last.BINARY);
    }

    @Override
    public void visit(BinaryMetadata binaryMetadata) {
        sb.append(" ").append(formatLogical(binaryMetadata.getOperator()));
        sb.append("\n");
        fieldIndent = true;
    }

    @Override
    public void end(BinaryMetadata binaryMetadata) {
        stack.pop();
        if (stack.isEmpty()) {
            sb.append("\n");
        }
    }

    @Override
    public void start(NaryMetadata naryMetadata) {
        stack.push(Last.NARY);
    }

    @Override
    public void visit(NaryMetadata naryMetadata) {
        sb.append(formatLogical(naryMetadata.getOperator()));
        sb.append("\n");
    }

    @Override
    public void end(NaryMetadata naryMetadata) {
        stack.pop();
        if (stack.isEmpty()) {
            sb.append("\n");
        }
    }

    @Override
    public void visit(ValidationRule validationRule) {
        sb.append(formatValidateWithMessage());
        sb.append(formatIndent(getCurrentIndentSize() + getIndentSize()));
        sb.append(formatValidationMessage(validationRule.getMessage()));
    }

    @Override
    public void visit(StepWhen stepWhen) {
        sb.append(formatWhen());
    }

    @Override
    public void visit(StepCondition stepCondition) {
    }

    public final int getCurrentIndentSize() {
        return getStackSize() * getIndentSize();
    }

    public int getIndentSize() {
        return INDENT_SIZE;
    }

    public String formatLogical(String binary) {
        return binary;
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