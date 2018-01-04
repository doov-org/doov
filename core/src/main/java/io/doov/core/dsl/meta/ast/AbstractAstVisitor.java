/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
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

    private final Deque<MetadataType> stack = new ArrayDeque<>();

    // Metadata

    @Override
    public final void visit(Metadata metadata, int depth) {
        visitMetadata(metadata, depth);
    }

    protected void visitMetadata(Metadata metadata, int depth) {
    }

    // FieldMetadata

    @Override
    public final void start(LeafMetadata metadata, int depth) {
        startMetadata(metadata, depth);
    }

    protected void startMetadata(LeafMetadata metadata, int depth) {
    }

    @Override
    public final void visit(LeafMetadata metadata, int depth) {
        visitMetadata(metadata, depth);
    }

    protected void visitMetadata(LeafMetadata metadata, int depth) {
    }

    @Override
    public final void end(LeafMetadata metadata, int depth) {
        endMetadata(metadata, depth);
    }

    protected void endMetadata(LeafMetadata metadata, int depth) {
    }

    // UnaryMetadata

    @Override
    public final void visit(UnaryMetadata metadata, int depth) {
        visitMetadata(metadata, depth);
    }

    protected void visitMetadata(UnaryMetadata metadata, int depth) {
    }

    // BinaryMetadata

    @Override
    public final void start(BinaryMetadata metadata, int depth) {
        startMetadata(metadata, depth);
        stack.push(MetadataType.BINARY_PREDICATE);
    }

    protected void startMetadata(BinaryMetadata metadata, int depth) {
    }

    @Override
    public final void visit(BinaryMetadata metadata, int depth) {
        visitMetadata(metadata, depth);
    }

    protected void visitMetadata(BinaryMetadata metadata, int depth) {
    }

    @Override
    public final void end(BinaryMetadata metadata, int depth) {
        stack.pop();
        endMetadata(metadata, depth);
    }

    protected void endMetadata(BinaryMetadata metadata, int depth) {
    }

    // NaryMetadata

    @Override
    public final void start(NaryMetadata metadata, int depth) {
        startMetadata(metadata, depth);
        stack.push(MetadataType.NARY_PREDICATE);
    }

    protected void startMetadata(NaryMetadata metadata, int depth) {
    }

    @Override
    public final void visit(NaryMetadata metadata, int depth) {
        visitMetadata(metadata, depth);
    }

    protected void visitMetadata(NaryMetadata metadata, int depth) {
    }

    @Override
    public final void end(NaryMetadata metadata, int depth) {
        stack.pop();
        endMetadata(metadata, depth);
    }

    protected void endMetadata(NaryMetadata metadata, int depth) {
    }

    // ValidationRule

    @Override
    public final void start(ValidationRule metadata, int depth) {
        startMetadata(metadata, depth);
        stack.push(MetadataType.RULE);
    }

    protected void startMetadata(ValidationRule metadata, int depth) {
    }

    @Override
    public final void visit(ValidationRule metadata, int depth) {
        visitMetadata(metadata, depth);
    }

    protected void visitMetadata(ValidationRule metadata, int depth) {
    }

    @Override
    public final void end(ValidationRule metadata, int depth) {
        stack.pop();
        endMetadata(metadata, depth);
    }

    protected void endMetadata(ValidationRule metadata, int depth) {
    }

    // StepWhen

    @Override
    public final void start(StepWhen metadata, int depth) {
        startMetadata(metadata, depth);
        stack.push(MetadataType.WHEN);
    }

    protected void startMetadata(StepWhen metadata, int depth) {
    }

    @Override
    public final void visit(StepWhen metadata, int depth) {
        visitMetadata(metadata, depth);

    }

    protected void visitMetadata(StepWhen metadata, int depth) {
    }

    @Override
    public final void end(StepWhen metadata, int depth) {
        stack.pop();
        endMetadata(metadata, depth);
    }

    protected void endMetadata(StepWhen metadata, int depth) {
    }

    // StepCondition

    @Override
    public final void visit(StepCondition metadata, int depth) {
        visitMetadata(metadata, depth);
    }

    protected void visitMetadata(StepCondition metadata, int depth) {
    }

    // Implementation

    protected int getIndentSize() {
        return 0;
    }

    protected int getCurrentIndentSize() {
        return stack.size() * getIndentSize();
    }

    protected String formatCurrentIndent() {
        return IntStream.range(0, getCurrentIndentSize()).mapToObj(i -> " ").collect(joining(""));
    }

    protected String formatNewLine() {
        return "\n";
    }

    protected final MetadataType stackPeek() {
        return stack.peek();
    }

    protected final Stream<MetadataType> stackSteam() {
        return stack.stream();
    }

}
