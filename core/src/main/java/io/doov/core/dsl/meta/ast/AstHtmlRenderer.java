/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.Optional;

import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class AstHtmlRenderer extends HtmlWriter {

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
                default:
                    throw new IllegalStateException(metadata.type().name());
            }
        } finally {
            parents.pop();
        }
    }

    private void rule(Metadata metadata, ArrayDeque<Metadata> parents) {
        writeBeginDiv(CSS_VALIDATION_RULE);
        writeBeginSpan(CSS_WHEN);
        writeFromBundle(metadata.getOperator());
        writeEndSpan();
        metadata.children().forEach(m -> toHtml(m, parents));
        writeEndDiv();
    }

    private void binaryPredicate(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (pmd.map(m -> m.type() == BINARY_PREDICATE).orElse(false)) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_field_true_true_failure()
            writeExclusionBar((PredicateMetadata) metadata, ExclusionBar.SMALL);
            toHtml(metadata.childAt(0), parents);
            writeBeginSpan(CSS_OPERATOR);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            toHtml(metadata.childAt(1), parents);
        } else {
            writeBeginLi(CSS_LI_BINARY);
            toHtml(metadata.childAt(0), parents);
            writeBeginSpan(CSS_BINARY);
            writeFromBundle(metadata.getOperator());
            writeEndSpan();
            toHtml(metadata.childAt(1), parents);
            writeEndLi();
        }
    }

    private void leafPredicate(Metadata metadata, ArrayDeque<Metadata> parents) {
        writeExclusionBar((PredicateMetadata) metadata, ExclusionBar.SMALL);
        for (Element e : ((LeafMetadata<?>) metadata).elements()) {
            switch (e.getType()) {
                case OPERATOR:
                    writeBeginSpan(CSS_OPERATOR);
                    break;
                case VALUE:
                    writeBeginSpan(CSS_VALUE);
                    break;
                default:
                    throw new IllegalStateException(e.getType().name());
            }
            writeFromBundle(e.getReadable().readable());
            writeEndSpan();
        }
    }

    private void fieldPredicate(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(2).findFirst();
        if (pmd.map(m -> m.type() != BINARY_PREDICATE).orElse(true)) {
            // @see io.doov.core.dsl.meta.ast.HtmlAndTest.and_field_true_true_failure()
            writeExclusionBar((PredicateMetadata) metadata, ExclusionBar.SMALL);
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
                default:
                    throw new IllegalStateException(e.getType().name());
            }
            writeFromBundle(e.getReadable().readable());
            writeEndSpan();
        }
    }

    private void leafValue(Metadata metadata, ArrayDeque<Metadata> parents) {
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
                default:
                    throw new IllegalStateException(e.getType().name());
            }
            writeFromBundle(e.getReadable().readable());
            writeEndSpan();
        }
    }
}
