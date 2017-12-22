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

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class AstFullVisitor extends AbstractAstVisitor {

    private static final int INDENT_SIZE = 4;
    private final StringBuilder sb;

    public AstFullVisitor(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void visitMetadata(Metadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit Metadata ").append(metadata);
        sb.append("\n");
    }

    @Override
    public void visitMetadata(FieldMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit FieldMetadata ").append(metadata).append(" = ");
        metadata.stream().map(Element::getReadable).forEach(sb::append);
        sb.append("\n");
    }

    @Override
    public void visitMetadata(UnaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit UnaryMetadata ").append(metadata).append(" = ").append(metadata.getOperator());
        sb.append("\n");
    }

    @Override
    public void startMetadata(BinaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("start BinaryMetadata ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit BinaryMetadata ").append(metadata).append(" = ").append(metadata.getOperator());
        sb.append("\n");
    }

    @Override
    public void endMetadata(BinaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("end BinaryMetadata ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void startMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("start NaryMetadata ").append(metadata).append(" = ").append(metadata.getOperator());
        sb.append("\n");
    }

    @Override
    public void visitMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit NaryMetadata ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void endMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append("end NaryMetadata ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void startMetadata(ValidationRule metadata) {
        sb.append(formatCurrentIndent());
        sb.append("start ValidationRule ").append(metadata).append(" = ").append("rule");
        sb.append("\n");
    }

    @Override
    public void visitMetadata(ValidationRule metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit ValidationRule ").append(metadata).append(" = ").append("validate with message");
        sb.append("\n");
    }

    @Override
    public void endMetadata(ValidationRule metadata) {
        sb.append(formatCurrentIndent());
        sb.append("end ValidationRule ").append(metadata).append(" = ").append(metadata.getMessage());
        sb.append("\n");
    }

    @Override
    public void startMetadata(StepWhen metadata) {
        sb.append(formatCurrentIndent());
        sb.append("start StepWhen ").append(metadata).append(" = ").append("when");
        sb.append("\n");
    }

    @Override
    public void visitMetadata(StepWhen metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit StepWhen ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void endMetadata(StepWhen metadata) {
        sb.append(formatCurrentIndent());
        sb.append("end StepWhen ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    public void visitMetadata(StepCondition metadata) {
        sb.append(formatCurrentIndent());
        sb.append("visit StepCondition ").append(metadata).append(" = ").append("---");
        sb.append("\n");
    }

    @Override
    protected int getIndentSize() {
        return INDENT_SIZE;
    }

}
