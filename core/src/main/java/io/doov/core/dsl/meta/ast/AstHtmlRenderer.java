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
import static io.doov.core.dsl.meta.ElementType.FIELD;
import static io.doov.core.dsl.meta.ElementType.STRING_VALUE;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.UNARY_PREDICATE;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
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
                    binaryPredicate(metadata, parents);
                    break;
                case LEAF_PREDICATE:
                    leafPredicate(metadata, parents);
                    break;
                case FIELD_PREDICATE:
                    fieldPredicate(metadata, parents);
                    break;
                case LEAF_VALUE:
                    leafValue(metadata, parents);
                    break;
                case UNARY_PREDICATE:
                    unaryPredicate(metadata, parents);
                    break;
                case NARY_PREDICATE:
                    naryPredicate(metadata, parents);
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
        write(SPACE);
        toHtml(metadata.childAt(0), parents);
        writeEndSpan();
    }

    private void fieldMatchAny(Metadata metadata, ArrayDeque<Metadata> parents) {
        writeBeginSpan(CSS_VALUE);
        for (Element e : ((LeafMetadata<?>) metadata).elements()) {
            writeFromBundle(escapeHtml4(e.getReadable().readable()));
        }
        writeEndSpan();
    }

    private void naryPredicate(Metadata metadata, ArrayDeque<Metadata> parents) {
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

    private void rule(Metadata metadata, ArrayDeque<Metadata> parents) {
        writeBeginDiv(CSS_VALIDATION_RULE);
        writeBeginSpan(CSS_WHEN);
        writeFromBundle(metadata.getOperator());
        writeEndSpan();
        write(SPACE);
        metadata.children().forEach(m -> toHtml(m, parents));
        writeEndDiv();
    }

    private void binaryPredicate(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (metadata.getOperator() == and && metadata.childAt(0).getOperator() == and
                && pmd.map(m -> m.getOperator() != and).orElse(true)) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_and_and()
            writeBeginLi(CSS_LI_BINARY);
            toHtml(metadata.childAt(0), parents);
            write(BR);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            write(SPACE);
            toHtml(metadata.childAt(1), parents);
            writeEndLi();
        } else if ((metadata.getOperator() == and && metadata.childAt(0).getOperator() == and) ||
                metadata.getOperator() == and && pmd.map(m -> m.getOperator() == and).orElse(false)){
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_and_and()
            toHtml(metadata.childAt(0), parents);
            write(BR);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            write(SPACE);
            toHtml(metadata.childAt(1), parents);
        } else if (pmd.map(m -> m.type() == BINARY_PREDICATE && !AND_OR.contains(metadata.getOperator()))
                .orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_field_true_true_failure()
            writeExclusionBar(metadata, parents);
            toHtml(metadata.childAt(0), parents);
            write(SPACE);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            write(SPACE);
            toHtml(metadata.childAt(1), parents);
        } else if (pmd.map(m -> m.type() == BINARY_PREDICATE && AND_OR.contains(metadata.getOperator()))
                .orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlOrTest.or_true_false_complex()
            writeBeginUl(CSS_UL_BINARY);
            writeBeginLi(CSS_LI_BINARY);
            toHtml(metadata.childAt(0), parents);
            write(BR);
            writeBeginSpan(CSS_BINARY);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            write(SPACE);
            toHtml(metadata.childAt(1), parents);
            writeEndLi();
            writeEndUl();
        } else if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false) && AND_OR.contains(metadata.getOperator())) {
            // @see io.doov.core.dsl.meta.ast.HtmlMatchAnyTest.matchAny_true_false_false_complex
            writeBeginLi(CSS_LI_BINARY);
            toHtml(metadata.childAt(0), parents);
            write(BR);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            write(SPACE);
            toHtml(metadata.childAt(1), parents);
            writeEndLi();
        } else if (AND_OR.contains(metadata.getOperator())) {
            // FIXME writeBeginUl(CSS_UL_BINARY);
            writeBeginLi(CSS_LI_BINARY);
            toHtml(metadata.childAt(0), parents);
            write(BR);
            writeBeginSpan(CSS_BINARY);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            write(SPACE);
            toHtml(metadata.childAt(1), parents);
            writeEndLi();
            // FIXME writeEndUl();
        } else if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlCountTest.count_field_true_true_failure()
            writeBeginLi(CSS_LI_BINARY);
            writeExclusionBar(metadata, parents);
            toHtml(metadata.childAt(0), parents);
            write(SPACE);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            write(SPACE);
            toHtml(metadata.childAt(1), parents);
            writeEndLi();
        } else if (pmd.map(m -> m.type() == UNARY_PREDICATE).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlCountTest.count_field_true_true_failure()
            writeBeginUl(CSS_UL_UNARY);
            writeExclusionBar(metadata, parents);
            toHtml(metadata.childAt(0), parents);
            write(SPACE);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            write(SPACE);
            toHtml(metadata.childAt(1), parents);
            writeEndUl();
        } else {
            // @see io.doov.core.dsl.meta.ast.HtmlCombinedTest.reduce_list()
            writeExclusionBar(metadata, parents);
            toHtml(metadata.childAt(0), parents);
            write(SPACE);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            write(SPACE);
            toHtml(metadata.childAt(1), parents);
        }

    }

    private void unaryPredicate(Metadata metadata, ArrayDeque<Metadata> parents) {
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

    // FIELD / véhicule / immatriculation du véhicule
    // FIELD / véhicule / code antecedent du véhicule
    // FIELD / véhicule / marque du véhicule
    // FIELD / véhicule / genre du véhicule
    // FIELD / véhicule / valeur du véhicule
    // FIELD / véhicule / carosserie du véhicule
    // FIELD / véhicule / groupe sra actuel du véhicule
    // FIELD / véhicule / trois roues véhicule
    // FIELD / véhicule / cylindree du véhicule
    // FIELD / véhicule / category du véhicule
    // FIELD / véhicule / clé pour AMV du véhicule
    // FIELD / véhicule / clé pour EuroAssurance du véhicule
    // UNKNOWN / -function- capitaux
    // UNKNOWN / -function- codes postaux non acceptés par mba mutuelle
    // UNKNOWN / -function- montant par emprunteur d'un prêt
    // UNKNOWN / -function- montant par emprunteur de l'autre prêt
    // UNKNOWN / -function- capitaux / -function- capitaux < 17000
    private void leafPredicate(Metadata metadata, ArrayDeque<Metadata> parents) {
        writeExclusionBar(metadata, parents);
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlSumTest.sum_sum_1_sum_2_greaterThan_3()
            writeBeginLi(CSS_LI_LEAF);
        }
        for (Element e : ((LeafMetadata<?>) metadata).elements()) {
            switch (e.getType()) {
                case OPERATOR:
                    writeBeginSpan(CSS_OPERATOR);
                    break;
                case VALUE:
                    write(SPACE);
                    writeBeginSpan(CSS_VALUE);
                    break;
                case FIELD:
                    writeBeginSpan(CSS_FIELD);
                    break;
                case UNKNOWN:
                    writeBeginSpan(CSS_UNKNOWN);
                    break;
                default:
                    throw new IllegalStateException(e.getType().name());
            }
            if (e.getType() == FIELD) {
                handleField((DslField<?>) e.getReadable());
            } else {
                writeFromBundle(escapeHtml4(e.getReadable().readable()));
            }
            writeEndSpan();
        }
        if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlSumTest.sum_sum_1_sum_2_greaterThan_3()
            writeEndLi();
        }
    }

    // TEMPORAL_UNIT / months / durée du prêt 1 month(s)
    // TEMPORAL_UNIT / months / durée du prêt 2 month(s)
    private void fieldPredicate(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        writeExclusionBar(metadata, parents);
        if (pmd.map(m -> m.type() == NARY_PREDICATE).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlSumTest.sum_sum_1_sum_2_greaterThan_3()
            writeBeginLi(CSS_LI_LEAF);
        }
        for (Element e : ((LeafMetadata<?>) metadata).elements()) {
            switch (e.getType()) {
                case OPERATOR:
                    writeBeginSpan(CSS_OPERATOR);
                    break;
                case VALUE:
                    writeBeginSpan(CSS_VALUE);
                    break;
                case FIELD:
                    writeBeginSpan(CSS_FIELD);
                    break;
                case TEMPORAL_UNIT:
                    writeBeginSpan(CSS_OPERATOR);
                    break;
                default:
                    throw new IllegalStateException(e.getType().name());
            }
            if (e.getType() == FIELD) {
                handleField((DslField<?>) e.getReadable());
            } else {
                writeFromBundle(escapeHtml4(e.getReadable().readable()));
            }
            writeEndSpan();
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

    private void leafValue(Metadata metadata, ArrayDeque<Metadata> parents) {
        for (Element e : ((LeafMetadata<?>) metadata).elements()) {
            switch (e.getType()) {
                case OPERATOR:
                    writeBeginSpan(CSS_OPERATOR);
                    break;
                case VALUE:
                case STRING_VALUE:
                    writeBeginSpan(CSS_VALUE);
                    break;
                case FIELD:
                    writeBeginSpan(CSS_FIELD);
                    break;
                case UNKNOWN:
                    writeBeginSpan(CSS_UNKNOWN);
                    break;
                case TEMPORAL_UNIT:
                    writeBeginSpan(CSS_OPERATOR);
                    break;
                default:
                    throw new IllegalStateException(e.getType().name());
            }
            if (e.getType() == STRING_VALUE)
                write(APOS);
            if (e.getType() == FIELD) {
                handleField((DslField<?>) e.getReadable());
            } else {
                writeFromBundle(escapeHtml4(e.getReadable().readable()));
            }
            if (e.getType() == STRING_VALUE)
                write(APOS);
            writeEndSpan();
        }
    }

}
