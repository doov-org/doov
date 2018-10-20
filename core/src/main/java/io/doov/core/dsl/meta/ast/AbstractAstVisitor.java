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
                    startWhen((WhenMetadata) metadata, depth);
                    break;
                case UNARY_PREDICATE:
                    startUnary((UnaryPredicateMetadata) metadata, depth);
                    break;
                case FIELD_PREDICATE:
                case FIELD_PREDICATE_MATCH_ANY:
                case LEAF_PREDICATE:
                    startLeaf((LeafPredicateMetadata<?>) metadata, depth);
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
                    startRule((RuleMetadata) metadata, depth);
                    break;
                case TYPE_CONVERTER:
                case TYPE_CONVERTER_IDENTITY:
                    startTypeConverter((ConverterMetadata) metadata, depth);
                    break;
                default:
                    startDefault(metadata, depth);
                    break;
            }
        } finally {
            stack.push(metadata.type());
        }
    }

    public final void visit(Metadata metadata, int depth) {
        switch (metadata.type()) {
            case WHEN:
                visitWhen((WhenMetadata) metadata, depth);
                break;
            case UNARY_PREDICATE:
                visitUnary((UnaryPredicateMetadata) metadata, depth);
                break;
            case FIELD_PREDICATE:
            case FIELD_PREDICATE_MATCH_ANY:
            case LEAF_PREDICATE:
                visitLeaf((LeafPredicateMetadata<?>) metadata, depth);
                break;
            case BINARY_PREDICATE:
                visitBinary((BinaryPredicateMetadata) metadata, depth);
                break;
            case MAPPING_INPUT:
            case MAPPING_LEAF:
            case SINGLE_MAPPING:
            case MULTIPLE_MAPPING:
            case THEN_MAPPING:
            case ELSE_MAPPING:
                visitMappingRule(metadata, depth);
                break;
            case NARY_PREDICATE:
                visitNary((NaryPredicateMetadata) metadata, depth);
                break;
            case RULE:
                visitRule((RuleMetadata) metadata, depth);
                break;
            case TYPE_CONVERTER:
            case TYPE_CONVERTER_IDENTITY:
                visitTypeConverter((ConverterMetadata) metadata, depth);
                break;
            default:
                visitDefault(metadata, depth);
                break;
        }
    }

    public final void end(Metadata metadata, int depth) {
        try {
            switch (metadata.type()) {
                case WHEN:
                    endWhen((WhenMetadata) metadata, depth);
                    break;
                case UNARY_PREDICATE:
                    endUnary((UnaryPredicateMetadata) metadata, depth);
                    break;
                case FIELD_PREDICATE:
                case FIELD_PREDICATE_MATCH_ANY:
                case LEAF_PREDICATE:
                    endLeaf((LeafPredicateMetadata<?>) metadata, depth);
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
                    endRule((RuleMetadata) metadata, depth);
                    break;
                case TYPE_CONVERTER:
                case TYPE_CONVERTER_IDENTITY:
                    endTypeConverter((ConverterMetadata) metadata, depth);
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

    public void visitDefault(Metadata metadata, int depth) {
    }

    public void endDefault(Metadata metadata, int depth) {
    }

    // LeafMetadata

    public void startLeaf(LeafPredicateMetadata<?> metadata, int depth) {
    }

    public void visitLeaf(LeafPredicateMetadata<?> metadata, int depth) {
    }

    public void endLeaf(LeafPredicateMetadata<?> metadata, int depth) {
    }

    // UnaryMetadata
    public void startUnary(UnaryPredicateMetadata metadata, int depth) {
    }

    public void visitUnary(UnaryPredicateMetadata metadata, int depth) {
    }

    public void endUnary(UnaryPredicateMetadata metadata, int depth) {
    }

    // BinaryMetadata

    public void startBinary(BinaryPredicateMetadata metadata, int depth) {
    }

    public void visitBinary(BinaryPredicateMetadata metadata, int depth) {
    }

    public void endBinary(BinaryPredicateMetadata metadata, int depth) {
    }

    // NaryMetadata

    public void startNary(NaryPredicateMetadata metadata, int depth) {
    }

    public void visitNary(NaryPredicateMetadata metadata, int depth) {
    }

    public void endNary(NaryPredicateMetadata metadata, int depth) {
    }

    // ValidationRule

    public void startRule(RuleMetadata metadata, int depth) {
    }

    public void visitRule(RuleMetadata metadata, int depth) {
    }

    public void endRule(RuleMetadata metadata, int depth) {
    }

    // StepWhen

    public void startWhen(WhenMetadata metadata, int depth) {
    }

    public void visitWhen(WhenMetadata metadata, int depth) {
    }

    public void endWhen(WhenMetadata metadata, int depth) {
    }

    // TypeConverter

    public void startTypeConverter(ConverterMetadata metadata, int depth) {
    }

    public void visitTypeConverter(ConverterMetadata metadata, int depth) {
    }

    public void endTypeConverter(ConverterMetadata metadata, int depth) {
    }

    // Mapping Rule

    public void startMappingRule(Metadata metadata, int depth) {
    }

    public void visitMappingRule(Metadata metadata, int depth) {
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
