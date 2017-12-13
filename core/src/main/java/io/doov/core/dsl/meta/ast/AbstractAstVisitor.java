/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl.meta.ast;

import static java.util.stream.Collectors.joining;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public abstract class AbstractAstVisitor implements MetadataVisitor {

    enum Element {
        RULE, WHEN, UNARY, BINARY, NARY
    }

    private final Deque<Element> stack = new ArrayDeque<>();

    private int newLineIndex = 0;

    final StringBuilder sb;

    public AbstractAstVisitor(StringBuilder stringBuilder) {
        sb = stringBuilder;
    }

    // Metadata

    @Override
    public final void visit(Metadata metadata) {
        visitMetadata(metadata);
    }

    protected void visitMetadata(Metadata metadata) {
    }

    // FieldMetadata

    @Override
    public final void start(FieldMetadata metadata) {
        startMetadata(metadata);
    }

    protected void startMetadata(FieldMetadata metadata) {
    }

    @Override
    public final void visit(FieldMetadata metadata) {
        visitMetadata(metadata);
    }

    protected void visitMetadata(FieldMetadata metadata) {
    }

    @Override
    public final void end(FieldMetadata metadata) {
        endMetadata(metadata);
    }

    protected void endMetadata(FieldMetadata metadata) {
    }

    // UnaryMetadata

    @Override
    public final void visit(UnaryMetadata metadata) {
        visitMetadata(metadata);
    }

    protected void visitMetadata(UnaryMetadata metadata) {
    }

    // BinaryMetadata

    @Override
    public final void start(BinaryMetadata metadata) {
        startMetadata(metadata);
        stack.push(Element.BINARY);
    }

    protected void startMetadata(BinaryMetadata metadata) {
    }

    @Override
    public final void visit(BinaryMetadata metadata) {
        visitMetadata(metadata);
    }

    protected void visitMetadata(BinaryMetadata metadata) {
    }

    @Override
    public final void end(BinaryMetadata metadata) {
        stack.pop();
        endMetadata(metadata);
    }

    protected void endMetadata(BinaryMetadata metadata) {
    }

    // NaryMetadata

    @Override
    public final void start(NaryMetadata metadata) {
        startMetadata(metadata);
        stack.push(Element.NARY);
    }

    protected void startMetadata(NaryMetadata metadata) {
    }

    @Override
    public final void visit(NaryMetadata metadata) {
        visitMetadata(metadata);
    }

    protected void visitMetadata(NaryMetadata metadata) {
    }

    @Override
    public final void end(NaryMetadata metadata) {
        stack.pop();
        endMetadata(metadata);
    }

    protected void endMetadata(NaryMetadata metadata) {
    }

    // ValidationRule

    @Override
    public final void start(ValidationRule metadata) {
        startMetadata(metadata);
        stack.push(Element.RULE);
    }

    protected void startMetadata(ValidationRule metadata) {
    }

    @Override
    public final void visit(ValidationRule metadata) {
        visitMetadata(metadata);
    }

    protected void visitMetadata(ValidationRule metadata) {
    }

    @Override
    public final void end(ValidationRule metadata) {
        stack.pop();
        endMetadata(metadata);
    }

    protected void endMetadata(ValidationRule metadata) {
    }

    // StepWhen

    @Override
    public final void start(StepWhen metadata) {
        startMetadata(metadata);
        stack.push(Element.WHEN);
    }

    protected void startMetadata(StepWhen metadata) {
    }

    @Override
    public final void visit(StepWhen metadata) {
        visitMetadata(metadata);

    }

    protected void visitMetadata(StepWhen metadata) {
    }

    @Override
    public final void end(StepWhen metadata) {
        stack.pop();
        endMetadata(metadata);
    }

    protected void endMetadata(StepWhen metadata) {
    }

    // StepCondition

    @Override
    public final void visit(StepCondition metadata) {
        visitMetadata(metadata);
    }

    protected void visitMetadata(StepCondition metadata) {
    }

    // Implementation

    protected int getIndentSize() {
        return 0;
    }

    protected int getNewLineIndex() {
        return newLineIndex;
    }

    protected int getCurrentIndentSize() {
        return stack.size() * getIndentSize();
    }

    protected String formatCurrentIndent() {
        return IntStream.range(0, getCurrentIndentSize()).mapToObj(i -> " ").collect(joining(""));
    }

    protected String formatNewLine() {
        newLineIndex = sb.length();
        return "\n";
    }

    protected final Element stackPeek() {
        return stack.peek();
    }

    protected final Stream<Element> stackSteam() {
        return stack.stream();
    }

}
