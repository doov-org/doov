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

import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.not;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.DefaultOperator.validate;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.UNARY_PREDICATE;
import static io.doov.core.dsl.meta.ReturnType.BOOLEAN;
import static io.doov.core.dsl.meta.ast.HtmlWriter.*;
import static java.util.Arrays.asList;

import java.util.*;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.*;

public class AstHtmlRenderer {
    private static final List<Operator> AND_OR = asList(and, or);

    protected HtmlWriter writer;

    public AstHtmlRenderer(HtmlWriter writer) {
        this.writer = writer;
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
                case TEMPLATE_PARAM:
                    binary(metadata, parents);
                    break;
                case LEAF_PREDICATE:
                case FIELD_PREDICATE:
                case LEAF_VALUE:
                case MAPPING_LEAF:
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
                case SINGLE_MAPPING:
                    singleMapping(metadata, parents);
                    break;
                default:
                    throw new IllegalStateException(metadata.type().name());
            }
        } finally {
            parents.pop();
        }
    }

    private void singleMapping(Metadata metadata, ArrayDeque<Metadata> parents) {
        writer.writeBeginSpan(CSS_SINGLE_MAPPING);
        toHtml(metadata.childAt(0), parents);
        writer.writeEndSpan();
    }

    private void when(Metadata metadata, ArrayDeque<Metadata> parents) {
        writer.writeBeginSpan(CSS_WHEN);
        writer.writeFromBundle(metadata.getOperator());
        writer.writeEndSpan();
        writer.writeBeginUl(CSS_UL_WHEN);
        toHtml(metadata.childAt(0), parents);
        writer.writeEndUl();
        writer.writeBeginSpan(CSS_VALIDATE);
        writer.writeFromBundle(validate);
        writer.writeEndSpan();
    }

    private void fieldMatchAny(Metadata metadata, ArrayDeque<Metadata> parents) {
        writer.writeBeginSpan(CSS_VALUE);
        for (Element e : ((LeafMetadata<?>) metadata).elements()) {
            writer.writeFromBundle(writer.escapeHtml4(e.getReadable().readable()));
        }
        writer.writeEndSpan();
    }

    private void nary(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (pmd.map(m -> m.getOperator().returnType() == BOOLEAN).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_and_count()
            writer.writeExclusionBar(metadata, parents);
            writer.writeBeginSpan(CSS_NARY);
            writer.writeFromBundle(metadata.getOperator());
            writer.writeEndSpan();
            writer.writeBeginOl(CSS_OL_NARY);
            metadata.children().forEach(m -> toHtml(m, parents));
            writer.writeEndOl();
        } else {
            writer.writeBeginLi(CSS_LI_NARY);
            writer.writeExclusionBar(metadata, parents);
            writer.writeBeginSpan(CSS_NARY);
            writer.writeFromBundle(metadata.getOperator());
            writer.writeEndSpan();
            writer.writeBeginOl(CSS_OL_NARY);
            metadata.children().forEach(m -> toHtml(m, parents));
            writer.writeEndOl();
            writer.writeEndLi();
        }
    }

    private void rule(Metadata metadata, ArrayDeque<Metadata> parents) {
        writer.writeBeginDiv(CSS_VALIDATION_RULE);
        metadata.children().forEach(m -> toHtml(m, parents));
        writer.writeEndDiv();
    }

    private void binary(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        final MetadataType pmdType = pmd.map(Metadata::type).orElse(null);
        final Operator pmdOperator = pmd.map(Metadata::getOperator).orElse(null);
        final boolean leftChild = pmd.map(m -> m.childAt(0) == metadata).orElse(false);
        if (!AND_OR.contains(pmdOperator) && metadata.getOperator() == and) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_and_and()
            writer.writeBeginLi(CSS_LI_BINARY);
            binary_BR(metadata, parents);
            writer.writeEndLi();
        } else if (pmdOperator == and && metadata.getOperator() == and) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_and_and()
            binary_BR(metadata, parents);
        } else if (!AND_OR.contains(pmdOperator) && metadata.getOperator() == or) {
            // @see io.doov.core.dsl.meta.ast.HtmlOrTest.or_or_or()
            writer.writeBeginLi(CSS_LI_BINARY);
            binary_BR(metadata, parents);
            writer.writeEndLi();
        } else if (pmdOperator == or && metadata.getOperator() == or) {
            // @see io.doov.core.dsl.meta.ast.HtmlOrTest.or_or_or()
            binary_BR(metadata, parents);
        } else if (pmdOperator == or && metadata.getOperator() == and && leftChild) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_or_and()
            binary_SPACE(metadata, parents);
        } else if (pmdOperator == and && metadata.getOperator() == or && leftChild) {
            // @see io.doov.core.dsl.meta.ast.HtmlMoreCombinedTest.or_and_sum()
            binary_BR(metadata, parents);
        } else if (pmdType == BINARY_PREDICATE && AND_OR.contains(metadata.getOperator())) {
            // @see io.doov.core.dsl.meta.ast.HtmlOrTest.or_true_false_complex()
            writer.writeBeginUl(CSS_UL_BINARY);
            writer.writeBeginLi(CSS_LI_BINARY);
            binary_BR(metadata, parents);
            writer.writeEndLi();
            writer.writeEndUl();
        } else if (pmdType == BINARY_PREDICATE && !AND_OR.contains(metadata.getOperator())) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_field_true_true_failure()
            binary_SPACE(metadata, parents);
        } else if (pmdType == NARY_PREDICATE && AND_OR.contains(metadata.getOperator())) {
            // @see io.doov.core.dsl.meta.ast.HtmlMatchAnyTest.matchAny_true_false_false_complex
            writer.writeBeginLi(CSS_LI_BINARY);
            binary_BR(metadata, parents);
            writer.writeEndLi();
        } else if (pmdType == NARY_PREDICATE && !AND_OR.contains(metadata.getOperator())) {
            // @see io.doov.core.dsl.meta.ast.HtmlCountTest.count_field_true_true_failure()
            writer.writeBeginLi(CSS_LI_BINARY);
            binary_SPACE(metadata, parents);
            writer.writeEndLi();
        } else if (pmdType == UNARY_PREDICATE) {
            // @see io.doov.core.dsl.meta.ast.HtmlCountTest.count_field_true_true_failure()
            writer.writeBeginUl(CSS_UL_UNARY);
            binary_SPACE(metadata, parents);
            writer.writeEndUl();
        } else if (AND_OR.contains(metadata.getOperator())) {
            writer.writeBeginLi(CSS_LI_BINARY);
            binary_BR(metadata, parents);
            writer.writeEndLi();
        } else {
            // @see io.doov.core.dsl.meta.ast.HtmlCombinedTest.reduce_list()
            binary_SPACE(metadata, parents);
        }

    }

    private void binary_BR(Metadata metadata, ArrayDeque<Metadata> parents) {
        toHtml(metadata.childAt(0), parents);
        writer.write(BR);
        writer.writeBeginSpan(CSS_OPERATOR);
        writer.writeFromBundle(metadata.getOperator());
        writer.writeEndSpan();
        writer.write(SPACE);
        toHtml(metadata.childAt(1), parents);
    }

    private void binary_SPACE(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        final Operator pmdOperator = pmd.map(Metadata::getOperator).orElse(null);
        final boolean leftChild = pmd.map(m -> m.childAt(0) == metadata).orElse(false);
        if ((!leftChild || pmdOperator != or || metadata.getOperator() != and)) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_or_and()
            writer.writeExclusionBar(metadata, parents);
        }
        toHtml(metadata.childAt(0), parents);
        writer.write(SPACE);
        writer.writeBeginSpan(CSS_OPERATOR);
        writer.writeFromBundle(metadata.getOperator());
        writer.writeEndSpan();
        writer.write(SPACE);
        toHtml(metadata.childAt(1), parents);
    }

    private void unary(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        final Operator pmdOperator = pmd.map(Metadata::getOperator).orElse(null);
        if (AND_OR.contains(pmdOperator) && metadata.getOperator() == not) {
            // @see io.doov.core.dsl.meta.ast.HtmlMoreCombinedTest.and_and_and_match_any_and_and()
            writer.writeExclusionBar(metadata, parents);
            prefixUnary(metadata, parents);
        } else if (!AND_OR.contains(pmdOperator) && metadata.getOperator() == not) {
            writer.writeBeginLi(CSS_LI_UNARY);
            prefixUnary(metadata, parents);
            writer.writeEndLi();
        } else if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false)) {
            // @see io.doov.sample.validation.ast.HtmlSampleRulesTest.RULE_USER()
            writer.writeBeginLi(CSS_LI_LEAF);
            writer.writeExclusionBar(metadata, parents);
            postfixUnary(metadata, parents);
            writer.writeEndLi();
        } else {
            // @see io.doov.core.dsl.meta.ast.HtmlCombinedTest.reduce_null()
            writer.writeExclusionBar(metadata, parents);
            postfixUnary(metadata, parents);
        }
    }

    private void prefixUnary(Metadata metadata, ArrayDeque<Metadata> parents) {
        writer.writeBeginSpan(CSS_OPERATOR);
        writer.writeFromBundle(metadata.getOperator());
        writer.writeEndSpan();
        writer.write(SPACE);
        toHtml(metadata.childAt(0), parents);
    }

    private void postfixUnary(Metadata metadata, ArrayDeque<Metadata> parents) {
        toHtml(metadata.childAt(0), parents);
        writer.write(SPACE);
        writer.writeBeginSpan(CSS_OPERATOR);
        writer.writeFromBundle(metadata.getOperator());
        writer.writeEndSpan();
    }

    private void leaf(Metadata metadata, ArrayDeque<Metadata> parents) {
        writer.writeExclusionBar(metadata, parents);
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlSumTest.sum_sum_1_sum_2_greaterThan_3()
            writer.writeBeginLi(CSS_LI_LEAF);
        }
        final List<Element> elts = new ArrayList<Element>(((LeafMetadata<?>) metadata).elements());
        for (Element e : elts) {
            writer.writeBeginSpan(spanClass(e.getType()));
            switch (e.getType()) {
                case OPERATOR:
                    writer.writeFromBundle((Operator) e.getReadable());
                    break;
                case TEMPORAL_UNIT:
                    writer.writeFromBundle(e.getReadable().readable());
                    break;
                case FIELD:
                    handleField((DslField<?>) e.getReadable());
                    break;
                case STRING_VALUE:
                    writer.write(APOS);
                    writer.write(writer.escapeHtml4(e.getReadable().readable()));
                    writer.write(APOS);
                    break;
                default:
                    writer.writeFromBundle(writer.escapeHtml4(e.getReadable().readable()));
            }
            writer.writeEndSpan();
            if (elts.indexOf(e) != elts.size() - 1)
                writer.write(SPACE);
        }
        if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlSumTest.sum_sum_1_sum_2_greaterThan_3()
            writer.writeEndLi();
        }
    }

    /**
     * Allows to overrides the default behaviour of the HTML renderer like adding links of tooltip.
     *
     * @param field the field
     */
    protected void handleField(DslField<?> field) {
        writer.writeFromBundle(writer.escapeHtml4(field.readable()));
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
