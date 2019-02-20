/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.not;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.DefaultOperator.validate;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.UNARY_PREDICATE;
import static io.doov.core.dsl.meta.ReturnType.BOOLEAN;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.ElementType;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.i18n.ResourceProvider;

public class AstHtmlRenderer extends HtmlWriter {
    private static final List<Operator> AND_OR = asList(and, or);

    public static String toHtml(Metadata metadata, Locale locale) {
        final ByteArrayOutputStream ops = new ByteArrayOutputStream();
        new AstHtmlRenderer(locale, ops, BUNDLE).toHtml(metadata);
        return new String(ops.toByteArray(), UTF_8);
    }

    public AstHtmlRenderer(Locale locale, OutputStream os, ResourceProvider resources) {
        super(locale, os, resources);
    }

    public void toHtml(Metadata metadata) {
        toHtml(metadata, new ArrayDeque<>());
    }

    private void toHtml(Metadata metadata, ArrayDeque<Metadata> parents) {
        parents.push(metadata);
        try {
            switch (metadata.type()) {
                case RULE:
                    rule(metadata, parents);
                    break;
                case WHEN:
                    when(metadata, parents);
                    break;
                case BINARY_PREDICATE:
                    binary(metadata, parents);
                    break;
                case LEAF_PREDICATE:
                case FIELD_PREDICATE:
                case LEAF_VALUE:
                    leaf(metadata, parents);
                    break;
                case UNARY_PREDICATE:
                    unary(metadata, parents);
                    break;
                case NARY_PREDICATE:
                    nary(metadata, parents);
                    break;
                case FIELD_PREDICATE_MATCH_ANY:
                    fieldMatchAny(metadata, parents);
                    break;
                default:
                    throw new IllegalStateException(metadata.type().name());
            }
        } finally {
            parents.pop();
        }
    }

    private void when(Metadata metadata, ArrayDeque<Metadata> parents) {
        writeBeginSpan(CSS_WHEN);
        writeFromBundle(metadata.getOperator());
        writeEndSpan();
        writeBeginUl(CSS_UL_WHEN);
        toHtml(metadata.childAt(0), parents);
        writeEndUl();
        writeBeginSpan(CSS_VALIDATE);
        writeFromBundle(validate);
        writeEndSpan();
    }

    private void fieldMatchAny(Metadata metadata, ArrayDeque<Metadata> parents) {
        writeBeginSpan(CSS_VALUE);
        for (Element e : ((LeafMetadata<?>) metadata).elements()) {
            writeFromBundle(escapeHtml4(e.getReadable().readable()));
        }
        writeEndSpan();
    }

    private void nary(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (pmd.map(m -> m.getOperator().returnType() == BOOLEAN).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_and_count()
            writeExclusionBar(metadata, parents);
            writeBeginSpan(CSS_NARY);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            writeBeginOl(CSS_OL_NARY);
            metadata.children().forEach(m -> toHtml(m, parents));
            writeEndOl();
        } else {
            writeBeginLi(CSS_LI_NARY);
            writeExclusionBar(metadata, parents);
            writeBeginSpan(CSS_NARY);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            writeBeginOl(CSS_OL_NARY);
            metadata.children().forEach(m -> toHtml(m, parents));
            writeEndOl();
            writeEndLi();
        }
    }

    private void rule(Metadata metadata, ArrayDeque<Metadata> parents) {
        writeBeginDiv(CSS_VALIDATION_RULE);
        metadata.children().forEach(m -> toHtml(m, parents));
        writeEndDiv();
    }

    private void binary(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        final MetadataType pmdType = pmd.map(Metadata::type).orElse(null);
        final Operator pmdOperator = pmd.map(Metadata::getOperator).orElse(null);

        if (metadata.getOperator() == and
                && metadata.childAt(0).getOperator().returnType() == BOOLEAN
                && pmdOperator != and) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_and_and()
            writeBeginLi(CSS_LI_BINARY);
            binary_BR(metadata, parents);
            writeEndLi();
        } else if ((metadata.getOperator() == and && metadata.childAt(0).getOperator().returnType() == BOOLEAN) ||
                (metadata.getOperator() == and && pmdOperator == and)) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_and_and()
            binary_BR(metadata, parents);
        } else if (metadata.getOperator() == or
                && metadata.childAt(0).getOperator().returnType() == BOOLEAN
                && pmdOperator != or) {
            // @see io.doov.core.dsl.meta.ast.HtmlOrTest.or_or_or()
            writeBeginLi(CSS_LI_BINARY);
            binary_BR(metadata, parents);
            writeEndLi();
        } else if ((metadata.getOperator() == or && metadata.childAt(0).getOperator().returnType() == BOOLEAN) ||
                (metadata.getOperator() == or && pmdOperator == or)) {
            // @see io.doov.core.dsl.meta.ast.HtmlOrTest.or_or_or()
            binary_BR(metadata, parents);
        } else if (pmdType == BINARY_PREDICATE && AND_OR.contains(metadata.getOperator())) {
            // @see io.doov.core.dsl.meta.ast.HtmlOrTest.or_true_false_complex()
            writeBeginUl(CSS_UL_BINARY);
            writeBeginLi(CSS_LI_BINARY);
            binary_BR(metadata, parents);
            writeEndLi();
            writeEndUl();
        } else if (pmdType == BINARY_PREDICATE && !AND_OR.contains(metadata.getOperator())) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_field_true_true_failure()
            binary_SPACE(metadata, parents);
        } else if (pmdType == NARY_PREDICATE && AND_OR.contains(metadata.getOperator())) {
            // @see io.doov.core.dsl.meta.ast.HtmlMatchAnyTest.matchAny_true_false_false_complex
            writeBeginLi(CSS_LI_BINARY);
            binary_BR(metadata, parents);
            writeEndLi();
        } else if (pmdType == NARY_PREDICATE && !AND_OR.contains(metadata.getOperator())) {
            // @see io.doov.core.dsl.meta.ast.HtmlCountTest.count_field_true_true_failure()
            writeBeginLi(CSS_LI_BINARY);
            binary_SPACE(metadata, parents);
            writeEndLi();
        } else if (pmdType == UNARY_PREDICATE) {
            // @see io.doov.core.dsl.meta.ast.HtmlCountTest.count_field_true_true_failure()
            writeBeginUl(CSS_UL_UNARY);
            binary_SPACE(metadata, parents);
            writeEndUl();
        } else if (AND_OR.contains(metadata.getOperator())) {
            writeBeginLi(CSS_LI_BINARY);
            binary_BR(metadata, parents);
            writeEndLi();
        } else {
            // @see io.doov.core.dsl.meta.ast.HtmlCombinedTest.reduce_list()
            binary_SPACE(metadata, parents);
        }

    }

    private void binary_BR(Metadata metadata, ArrayDeque<Metadata> parents) {
        toHtml(metadata.childAt(0), parents);
        write(BR);
        writeBeginSpan(CSS_OPERATOR);
        writeFromBundle(metadata.getOperator());
        writeEndSpan();
        write(SPACE);
        toHtml(metadata.childAt(1), parents);
    }

    private void binary_SPACE(Metadata metadata, ArrayDeque<Metadata> parents) {
        writeExclusionBar(metadata, parents);
        toHtml(metadata.childAt(0), parents);
        write(SPACE);
        writeBeginSpan(CSS_OPERATOR);
        writeFromBundle(metadata.getOperator());
        writeEndSpan();
        write(SPACE);
        toHtml(metadata.childAt(1), parents);
    }

    private void unary(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (metadata.getOperator() == not) {
            writeBeginLi(CSS_LI_UNARY);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            write(SPACE);
            toHtml(metadata.childAt(0), parents);
            writeEndLi();
        } else if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false)) {
            // @see io.doov.sample.validation.ast.HtmlSampleRulesTest.RULE_USER()
            writeBeginLi(CSS_LI_LEAF);
            writeExclusionBar(metadata, parents);
            toHtml(metadata.childAt(0), parents);
            write(SPACE);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            writeEndLi();
        } else {
            // @see io.doov.core.dsl.meta.ast.HtmlCombinedTest.reduce_null()
            writeExclusionBar(metadata, parents);
            toHtml(metadata.childAt(0), parents);
            write(SPACE);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
        }
    }

    private void leaf(Metadata metadata, ArrayDeque<Metadata> parents) {
        writeExclusionBar(metadata, parents);
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlSumTest.sum_sum_1_sum_2_greaterThan_3()
            writeBeginLi(CSS_LI_LEAF);
        }
        final List<Element> elts = new ArrayList<Element>(((LeafMetadata<?>) metadata).elements());
        for (Element e : elts) {
            writeBeginSpan(spanClass(e.getType()));
            switch (e.getType()) {
                case OPERATOR:
                    writeFromBundle((Operator) e.getReadable());
                    break;
                case TEMPORAL_UNIT:
                    writeFromBundle(e.getReadable().readable());
                    break;
                case FIELD:
                    handleField((DslField<?>) e.getReadable());
                    break;
                case STRING_VALUE:
                    write(APOS);
                    write(escapeHtml4(e.getReadable().readable()));
                    write(APOS);
                    break;
                default:
                    writeFromBundle(escapeHtml4(e.getReadable().readable()));
            }
            writeEndSpan();
            if (elts.indexOf(e) != elts.size() - 1)
                write(SPACE);
        }
        if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlSumTest.sum_sum_1_sum_2_greaterThan_3()
            writeEndLi();
        }
    }

    /**
     * Allows to overrides the default behaviour of the HTML renderer like adding links of tooltip.
     * 
     * @param field the field
     */
    protected void handleField(DslField<?> field) {
        writeFromBundle(escapeHtml4(field.readable()));
    }

    private static String spanClass(ElementType type) {
        switch (type) {
            case OPERATOR:
            case TEMPORAL_UNIT:
                return CSS_OPERATOR;
            case VALUE:
            case STRING_VALUE:
                return CSS_VALUE;
            case FIELD:
                return CSS_FIELD;
            case UNKNOWN:
                return CSS_UNKNOWN;
            default:
                throw new IllegalStateException(type.name());
        }
    }
}
