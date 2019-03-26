/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static java.nio.charset.Charset.defaultCharset;

import java.io.*;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import io.doov.core.dsl.lang.DSLBuilder;
import io.doov.core.dsl.meta.ast.*;

class SampleWriter {
    private final Locale locale;
    private final File file;
    private final FileOutputStream fos;

    private SampleWriter(String path, Locale locale) throws FileNotFoundException {
        this.file = new File(path);
        this.fos  = new FileOutputStream(file);
        this.locale = locale;
    }

    public static SampleWriter of(String path, Locale locale) throws FileNotFoundException {
        return new SampleWriter(path, locale);
    }

    private void ioWrite(String content) throws IOException {
        IOUtils.write(content,fos,defaultCharset());
    }

    private String css() throws IOException {
        return IOUtils.toString(AstVisitorUtils.class.getResourceAsStream("rules.css"), defaultCharset());
    }

    private String head(String css) {
        return "<head><meta charset=\"UTF-8\"/><style>" + css + "</style></head>";
    }

    void write(Iterable<? extends DSLBuilder> rules) throws IOException {

        ioWrite("<html>");
        ioWrite(head(css()));
        ioWrite("<body><div style='width:1024px; margin-left:20px;'>");

        for (DSLBuilder r : rules) {
            ioWrite("<div>" + r.readable(locale) + "</div>");
            ioWrite("<div>");
            new AstHtmlRenderer(new DefaultHtmlWriter(locale, fos, BUNDLE)).toHtml(r.metadata());
            ioWrite("<hr/>");
            ioWrite("</div>");
        }
        ioWrite("</body>");
        ioWrite("<html>");

        System.out.println("File written : " + file.getAbsolutePath());
    }
}
