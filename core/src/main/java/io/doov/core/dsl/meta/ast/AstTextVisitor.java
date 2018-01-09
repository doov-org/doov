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

import static io.doov.core.dsl.meta.DefaultOperator.empty;
import static io.doov.core.dsl.meta.DefaultOperator.rule;
import static io.doov.core.dsl.meta.DefaultOperator.validate_with_message;
import static io.doov.core.dsl.meta.DefaultOperator.when;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static java.util.stream.Collectors.joining;

import java.util.Locale;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.StepWhen;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.NaryMetadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.UnaryMetadata;

public class AstTextVisitor extends AbstractAstVisitor {

    private static final int INDENT_SIZE = 2;

    protected final StringBuilder sb;
    protected final ResourceProvider bundle;
    protected final Locale locale;
    protected int newLineIndex = 0;

    public AstTextVisitor(StringBuilder sb, ResourceProvider bundle, Locale locale) {
        this.sb = sb;
        this.bundle = bundle;
        this.locale = locale;
    }

    protected int getNewLineIndex() {
        return newLineIndex;
    }

    protected String formatNewLine() {
        newLineIndex = sb.length();
        return "\n";
    }

    @Override
    public void visitMetadata(LeafMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(formatLeafMetadata(metadata));
        sb.append(formatNewLine());
    }

    @Override
    public void visitMetadata(UnaryMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata, int depth) {
        sb.delete(getNewLineIndex(), sb.length());
        sb.append(" ");
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
    }

    @Override
    public void startMetadata(NaryMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
    }

    @Override
    public void startMetadata(ValidationRule metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(formatRule());
        sb.append(formatNewLine());
    }

    @Override
    public void visitMetadata(ValidationRule metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(formatValidateWithMessage());
        sb.append(" ");
        sb.append(formatMessage(metadata));
        sb.append(formatNewLine());
    }

    @Override
    public void startMetadata(StepWhen metadata, int depth) {
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
        if (BINARY_PREDICATE.equals(stackPeek())) {
            return (int) stackSteam().filter(e -> !BINARY_PREDICATE.equals(e)).count() *
                            getIndentSize();
        }
        return super.getCurrentIndentSize();
    }

    protected String formatLeafMetadata(LeafMetadata metadata) {
        return metadata.stream().map(e -> {
            switch (e.getType()) {
                case OPERATOR:
                    return bundle.get((Operator) e.getReadable(), locale);
                case FIELD:
                    return e.getReadable().readable();
                case STRING_VALUE:
                    return "'" + bundle.get(e.getReadable().readable(), locale) + "'";
                default:
                    return bundle.get(e.getReadable().readable(), locale);
            }
        }).collect(joining(" "));
    }

    protected String formatOperator(Operator operator) {
        return operator == null ? null : operator.readable();
    }

    protected String formatValue(Readable value) {
        return value == null ? null : value.readable();
    }

    protected String formatRule() {
        return bundle.get(rule, locale);
    }

    protected String formatValidateWithMessage() {
        return bundle.get(validate_with_message, locale);
    }

    protected String formatMessage(ValidationRule metadata) {
        String message = metadata.getMessage() == null ? bundle.get(empty, locale) : metadata.getMessage();
        return "'" + message + "'";
    }

    protected String formatWhen() {
        return bundle.get(when, locale);
    }

}
