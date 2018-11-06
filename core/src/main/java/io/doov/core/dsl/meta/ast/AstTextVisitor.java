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

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MappingOperator.*;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static java.util.stream.Collectors.joining;

import java.util.Locale;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import io.doov.core.dsl.meta.predicate.*;

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
    public void visitLeaf(LeafPredicateMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(formatLeafMetadata(metadata));
        sb.append(formatNewLine());
    }

    @Override
    public void visitUnary(UnaryPredicateMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
    }

    @Override
    public void visitBinary(BinaryPredicateMetadata metadata, int depth) {
        sb.delete(getNewLineIndex(), sb.length());
        sb.append(" ");
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
    }

    @Override
    public void startNary(NaryPredicateMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
    }

    @Override
    public void startRule(RuleMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(formatRule());
        sb.append(formatNewLine());
    }

    @Override
    public void visitRule(RuleMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(formatValidate());
        sb.append(formatNewLine());
    }

    @Override
    public void startWhen(WhenMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(formatWhen());
        sb.append(formatNewLine());
    }

    @Override
    public void startTypeConverter(ConverterMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(formatUsing());
        sb.append(formatNewLine());
    }

    @Override
    public void visitTypeConverter(ConverterMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(formatValue(metadata));
        sb.append(formatNewLine());
    }

    @Override
    public void startMappingRule(MappingMetadata metadata, int depth) {
        switch (metadata.type()) {
            case SINGLE_MAPPING:
                sb.append(formatCurrentIndent());
                sb.append(formatOperator(MappingOperator.map));
                sb.append(formatNewLine());
                break;
            case MULTIPLE_MAPPING:
                sb.append(formatCurrentIndent());
                sb.append(formatMappingMetadata(metadata));
                break;
            default:
                break;
        }
    }

    @Override
    public void visitMappingRule(MappingMetadata metadata, int depth) {
        switch (metadata.type()) {
            case MAPPING_INPUT:
                sb.append(formatCurrentIndent());
                sb.append(formatMappingMetadata(metadata));
                break;
            case MAPPING_OUTPUT:
                sb.append(formatCurrentIndent());
                sb.append(formatMappingMetadata(metadata));
                break;
            case SINGLE_MAPPING:
                sb.append(formatCurrentIndent());
                sb.append(formatOperator(MappingOperator.to));
                break;
            default:
                break;
        }
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

    protected String formatLeafMetadata(LeafPredicateMetadata metadata) {
        return metadata.elements().stream().map(e -> {
            switch (e.getType()) {
                case OPERATOR:
                    return bundle.get((Operator) e.getReadable(), locale);
                case FIELD:
                    return bundle.get(e.getReadable().readable(), locale);
                case STRING_VALUE:
                    return "'" + bundle.get(e.getReadable().readable(), locale) + "'";
                default:
                    return bundle.get(e.getReadable().readable(), locale);
            }
        }).collect(joining(" "));
    }

    protected String formatMappingMetadata(MappingMetadata metadata) {
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

    protected String formatValidate() {
        return bundle.get(validate, locale);
    }

    protected String formatWhen() {
        return bundle.get(when, locale);
    }

    private String formatUsing() {
        return bundle.get(using, locale);
    }

}
