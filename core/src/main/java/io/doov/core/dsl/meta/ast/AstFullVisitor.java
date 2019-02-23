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

import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.predicate.*;

public class AstFullVisitor extends AbstractAstVisitor {

    private static final int INDENT_SIZE = 4;
    private final StringBuilder sb;

    public AstFullVisitor(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void afterChildDefault(Metadata metadata, Metadata child, boolean hasNext, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("visit Metadata ").append(metadata);
        sb.append("\n");
    }

    @Override
    public void startLeaf(LeafMetadata<?> metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("start FieldMetadata ").append(metadata).append(" = ");
        metadata.elements().stream().map(Element::getReadable).forEach(sb::append);
        sb.append("\n");
    }

    @Override
    public void startUnary(UnaryPredicateMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("start UnaryMetadata ").append(metadata).append(" = ").append(metadata.getOperator());
        sb.append("\n");
    }

    @Override
    public void startBinary(BinaryPredicateMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("start BinaryMetadata ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void afterChildBinary(BinaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("visit BinaryMetadata ").append(metadata).append(" = ").append(metadata.getOperator());
        sb.append("\n");
    }

    @Override
    public void endBinary(BinaryPredicateMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("end BinaryMetadata ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void startNary(NaryPredicateMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("start NaryMetadata ").append(metadata).append(" = ").append(metadata.getOperator());
        sb.append("\n");
    }

    @Override
    public void afterChildNary(NaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("visit NaryMetadata ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void endNary(NaryPredicateMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("end NaryMetadata ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void startRule(Metadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("start ValidationRule ").append(metadata).append(" = ").append("rule");
        sb.append("\n");
    }

    @Override
    public void endRule(Metadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("end ValidationRule ").append(metadata).append(" = ").append("validate");
        sb.append("\n");
    }

    @Override
    public void startWhen(Metadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("start StepWhen ").append(metadata).append(" = ").append("when");
        sb.append("\n");
    }

    @Override
    public void endWhen(Metadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("end StepWhen ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void visitCondition(StepCondition metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append("visit StepCondition ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    protected int getIndentSize() {
        return INDENT_SIZE;
    }

}
