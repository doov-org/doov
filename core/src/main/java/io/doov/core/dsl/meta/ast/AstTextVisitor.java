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

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class AstTextVisitor extends AbstractAstVisitor {

    private static final int INDENT_SIZE = 2;

    public AstTextVisitor(StringBuilder stringBuilder) {
        super(stringBuilder);
    }

    @Override
    public void visitMetadata(FieldMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append(formatFieldMetadata(metadata));
        sb.append(formatNewLine());
    }

    @Override
    public void visitMetadata(UnaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append(formatOperator(metadata));
        sb.append(formatNewLine());
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata) {
        sb.delete(getNewLineIndex(), sb.length());
        sb.append(" ");
        sb.append(formatOperator(metadata));
        sb.append(formatNewLine());
    }

    @Override
    public void startMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append(formatOperator(metadata));
        sb.append(formatNewLine());
    }

    @Override
    public void startMetadata(ValidationRule metadata) {
        sb.append(formatCurrentIndent());
        sb.append(formatRule());
        sb.append(formatNewLine());
    }

    @Override
    public void visitMetadata(ValidationRule metadata) {
        sb.append(formatCurrentIndent());
        sb.append(formatValidateWithMessage());
        sb.append(" ");
        sb.append(formatMessage(metadata));
        sb.append(formatNewLine());
    }

    @Override
    public void startMetadata(StepWhen metadata) {
        sb.append(formatCurrentIndent());
        sb.append(formatWhen());
        sb.append(formatNewLine());
    }

    @Override
    protected int getIndentSize() {
        return INDENT_SIZE;
    }

    @Override
    protected int getCurrentIndentSize() {
        if (AbstractAstVisitor.Element.BINARY.equals(stackPeek())) {
            return (int) stackSteam().filter(e -> !AbstractAstVisitor.Element.BINARY.equals(e)).count() *
                    getIndentSize();
        }
        return super.getCurrentIndentSize();
    }

    protected String formatFieldMetadata(FieldMetadata metadata) {
        return metadata.stream()
                .map(FieldMetadata.Element::getReadable)
                .map(Readable::readable)
                .collect(joining(" "));
    }

    protected String formatField(Readable field) {
        return field == null ? null : field.readable();
    }

    protected String formatOperator(Readable operator) {
        return operator == null ? null : operator.readable();
    }

    protected String formatValue(Readable value) {
        return value == null ? null : value.readable();
    }

    protected String formatOperator(UnaryMetadata metadata) {
        return metadata.getOperator().readable();
    }

    protected String formatOperator(BinaryMetadata metadata) {
        return metadata.getOperator().readable();
    }

    protected String formatOperator(NaryMetadata metadata) {
        return metadata.getOperator().readable();
    }

    protected String formatRule() {
        return "Rule";
    }

    protected String formatValidateWithMessage() {
        return "validate with message";
    }

    protected String formatMessage(ValidationRule metadata) {
        String message = metadata.getMessage() == null ? "empty" : metadata.getMessage();
        return "'" + message + "'";
    }

    protected String formatWhen() {
        return "when";
    }

}
