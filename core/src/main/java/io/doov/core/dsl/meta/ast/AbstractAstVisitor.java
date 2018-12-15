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

import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.predicate.*;

public abstract class AbstractAstVisitor implements MetadataVisitor {

    private final Deque<MetadataType> stack = new ArrayDeque<>();

    // Metadata

    public final void start(Metadata metadata, int depth) {
        try {
            switch (metadata.type()) {
                case WHEN:
                    startWhen(metadata, depth);
                    break;
                case UNARY_PREDICATE:
                    startUnary((UnaryPredicateMetadata) metadata, depth);
                    break;
                case FIELD_PREDICATE:
                case FIELD_PREDICATE_MATCH_ANY:
                case LEAF_PREDICATE:
                    startLeaf((LeafMetadata<?>) metadata, depth);
                    break;
                case BINARY_PREDICATE:
                    startBinary((BinaryPredicateMetadata) metadata, depth);
                    break;
                case MAPPING_INPUT:
                case MAPPING_LEAF:
                case SINGLE_MAPPING:
                case MULTIPLE_MAPPING:
                case THEN_MAPPING:
                case ELSE_MAPPING:
                    startMappingRule(metadata, depth);
                    break;
                case NARY_PREDICATE:
                    startNary((NaryPredicateMetadata) metadata, depth);
                    break;
                case RULE:
                    startRule(metadata, depth);
                    break;
                case TYPE_CONVERTER:
                case TYPE_CONVERTER_IDENTITY:
                    startTypeConverter((LeafMetadata<?>) metadata, depth);
                    break;
                default:
                    startDefault(metadata, depth);
                    break;
            }
        } finally {
            stack.push(metadata.type());
        }
    }

    @Override
    public void beforeChild(Metadata metadata, Metadata child, int depth) {
        switch (metadata.type()) {
            case WHEN:
                beforeChildWhen(metadata, child, depth);
                break;
            case RULE:
                beforeChildRule(metadata, child, depth);
                break;
            case UNARY_PREDICATE:
                beforeChildUnary((UnaryPredicateMetadata) metadata, child, depth);
                break;
            case LEAF_PREDICATE:
                throw new IllegalStateException("no visit : there is no children");
            case FIELD_PREDICATE:
            case FIELD_PREDICATE_MATCH_ANY:
            case BINARY_PREDICATE:
                beforeChildBinary((BinaryPredicateMetadata) metadata, child, depth);
                break;
            case MAPPING_INPUT:
            case MAPPING_LEAF:
            case SINGLE_MAPPING:
            case MULTIPLE_MAPPING:
            case THEN_MAPPING:
            case ELSE_MAPPING:
                beforeChildMappingRule(metadata, child, depth);
                break;
            case NARY_PREDICATE:
                beforeChildNary((NaryPredicateMetadata) metadata, child, depth);
                break;
            case TYPE_CONVERTER:
            case TYPE_CONVERTER_IDENTITY:
                beforeChildTypeConverter((LeafMetadata<?>) metadata, child, depth);
                break;
            default:
                beforeChildDefault(metadata, depth);
                break;
        }
    }

    @Override
    public void afterChild(Metadata metadata, Metadata child, boolean hasNext, int depth) {
        switch (metadata.type()) {
            case WHEN:
                afterChildWhen(metadata, child, hasNext, depth);
                break;
            case UNARY_PREDICATE:
                afterChildUnary((UnaryPredicateMetadata) metadata, child, hasNext, depth);
                break;
            case RULE:
                afterChildRule(metadata, child, hasNext, depth);
                break;
            case LEAF_PREDICATE:
                throw new IllegalStateException("no visit : there is no children");
            case FIELD_PREDICATE:
            case FIELD_PREDICATE_MATCH_ANY:
            case BINARY_PREDICATE:
                afterChildBinary((BinaryPredicateMetadata) metadata, child, hasNext, depth);
                break;
            case MAPPING_INPUT:
            case MAPPING_LEAF:
            case SINGLE_MAPPING:
            case MULTIPLE_MAPPING:
            case THEN_MAPPING:
            case ELSE_MAPPING:
                afterChildMappingRule(metadata, child, hasNext, depth);
                break;
            case NARY_PREDICATE:
                afterChildNary((NaryPredicateMetadata) metadata, child, hasNext, depth);
                break;
            case TYPE_CONVERTER:
            case TYPE_CONVERTER_IDENTITY:
                afterChildTypeConverter((LeafMetadata<?>) metadata, child, hasNext, depth);
                break;
            default:
                afterChildDefault(metadata, child, hasNext, depth);
                break;
        }
    }

    public final void end(Metadata metadata, int depth) {
        try {
            switch (metadata.type()) {
                case WHEN:
                    endWhen(metadata, depth);
                    break;
                case UNARY_PREDICATE:
                    endUnary((UnaryPredicateMetadata) metadata, depth);
                    break;
                case FIELD_PREDICATE:
                case FIELD_PREDICATE_MATCH_ANY:
                case LEAF_PREDICATE:
                    endLeaf((LeafMetadata<?>) metadata, depth);
                    break;
                case BINARY_PREDICATE:
                    endBinary((BinaryPredicateMetadata) metadata, depth);
                    break;
                case MAPPING_INPUT:
                case MAPPING_LEAF:
                case SINGLE_MAPPING:
                case MULTIPLE_MAPPING:
                case THEN_MAPPING:
                case ELSE_MAPPING:
                    endMappingRule(metadata, depth);
                    break;
                case NARY_PREDICATE:
                    endNary((NaryPredicateMetadata) metadata, depth);
                    break;
                case RULE:
                    endRule(metadata, depth);
                    break;
                case TYPE_CONVERTER:
                case TYPE_CONVERTER_IDENTITY:
                    endTypeConverter((LeafMetadata<?>) metadata, depth);
                    break;
                default:
                    endDefault(metadata, depth);
                    break;
            }
        } finally {
            stack.pop();
        }
    }

    // DefaultMetadata

    public void startDefault(Metadata metadata, int depth) {
    }

    public void beforeChildDefault(Metadata metadata, int depth) {
    }

    public void afterChildDefault(Metadata metadata, Metadata child, boolean hasNext, int depth) {
    }

    public void endDefault(Metadata metadata, int depth) {
    }

    // LeafMetadata

    public void startLeaf(LeafMetadata<?> metadata, int depth) {
    }

    // visit leaf is impossible because there is no children

    public void endLeaf(LeafMetadata<?> metadata, int depth) {
    }

    // UnaryMetadata
    public void startUnary(UnaryPredicateMetadata metadata, int depth) {
    }

    public void beforeChildUnary(UnaryPredicateMetadata metadata, Metadata child, int depth) {
    }

    public void afterChildUnary(UnaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
    }

    public void endUnary(UnaryPredicateMetadata metadata, int depth) {
    }

    // BinaryMetadata

    public void startBinary(BinaryPredicateMetadata metadata, int depth) {
    }

    public void beforeChildBinary(BinaryPredicateMetadata metadata, Metadata child, int depth) {
    }

    public void afterChildBinary(BinaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
    }

    public void endBinary(BinaryPredicateMetadata metadata, int depth) {
    }

    // NaryMetadata

    public void startNary(NaryPredicateMetadata metadata, int depth) {
    }

    public void visitNary(NaryPredicateMetadata metadata, int depth) {
    }

    public void beforeChildNary(NaryPredicateMetadata metadata, Metadata child, int depth) {
    }

    public void afterChildNary(NaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
    }

    public void endNary(NaryPredicateMetadata metadata, int depth) {
    }

    // ValidationRule

    public void startRule(Metadata metadata, int depth) {
    }

    // visit rule is impossible because there is only one child
    public void beforeChildRule(Metadata metadata, Metadata child, int depth) {
    }

    public void afterChildRule(Metadata metadata, Metadata child, boolean hasNext, int depth) {
    }

    public void endRule(Metadata metadata, int depth) {
    }

    // StepWhen

    public void startWhen(Metadata metadata, int depth) {
    }

    public void beforeChildWhen(Metadata metadata, Metadata child, int depth) {
    }

    public void afterChildWhen(Metadata metadata, Metadata child, boolean hasNext, int depth) {
    }

    public void endWhen(Metadata metadata, int depth) {
    }

    // TypeConverter

    public void startTypeConverter(LeafMetadata<?> metadata, int depth) {
    }

    public void beforeChildTypeConverter(LeafMetadata<?> metadata, Metadata child, int depth) {
    }

    public void afterChildTypeConverter(LeafMetadata<?> metadata, Metadata child, boolean hasNext, int depth) {
    }

    public void endTypeConverter(LeafMetadata<?> metadata, int depth) {
    }

    // Mapping Rule

    public void startMappingRule(Metadata metadata, int depth) {
    }

    public void beforeChildMappingRule(Metadata metadata, Metadata child, int depth) {
    }

    public void afterChildMappingRule(Metadata metadata, Metadata child, boolean hasNext, int depth) {
    }

    public void endMappingRule(Metadata metadata, int depth) {
    }

    // StepCondition

    public void visitCondition(StepCondition metadata, int depth) {
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

    protected MetadataType stackPeek() {
        return stack.peek();
    }

    protected final Stream<MetadataType> stackSteam() {
        return stack.stream();
    }

}
