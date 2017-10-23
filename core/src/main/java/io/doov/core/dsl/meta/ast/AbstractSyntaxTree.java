package io.doov.core.dsl.meta.ast;

import java.util.*;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

abstract class AbstractSyntaxTree implements MetadataVisitor {

    enum Element {
        WHEN, RULE, UNARY, BINARY, NARY
    }

    private final Deque<Element> stack = new ArrayDeque<>();

    @Override
    public void visit(Metadata metadata) {
    }

    @Override
    public void visit(FieldMetadata fieldMetadata) {
    }

    @Override
    public void visit(UnaryMetadata unaryMetadata) {
    }

    @Override
    public void start(BinaryMetadata binaryMetadata) {
        stack.push(Element.BINARY);
    }

    @Override
    public void visit(BinaryMetadata binaryMetadata) {
    }

    @Override
    public void end(BinaryMetadata binaryMetadata) {
        stack.pop();
    }

    @Override
    public void start(NaryMetadata naryMetadata) {
        stack.push(Element.NARY);
    }

    @Override
    public void visit(NaryMetadata naryMetadata) {
    }

    @Override
    public void end(NaryMetadata naryMetadata) {
        stack.pop();
    }

    @Override
    public void start(ValidationRule validationRule) {
        stack.push(Element.RULE);
    }

    @Override
    public void visit(ValidationRule validationRule) {
    }

    @Override
    public void end(ValidationRule validationRule) {
        stack.pop();
    }

    @Override
    public void start(StepWhen stepWhen) {
        stack.push(Element.WHEN);
    }

    @Override
    public void visit(StepWhen stepWhen) {
    }

    @Override
    public void end(StepWhen stepWhen) {
        stack.pop();
    }

    @Override
    public void visit(StepCondition stepCondition) {
    }

    abstract int getIndentSize();

    final int getCurrentIndentSize() {
        return getStackSize() * getIndentSize();
    }

    final Element peek() {
        return stack.peek();
    }

    private int getStackSize() {
        return new HashSet<>(stack).size();
    }

}