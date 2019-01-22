/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.when;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.i18n.ResourceProvider;

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
        switch (metadata.type()) {
            case RULE:
                rule(metadata);
                break;
            default:
                throw new IllegalStateException(metadata.type().name());
        }
    }

    private void rule(Metadata metadata) {
        writeBeginDiv(CSS_CLASS_VALIDATION_RULE);
        writeBeginSpan(CSS_CLASS_WHEN);
        writeOperator(when);
        writeEndSpan();
        writeEndDiv();
    }
}
