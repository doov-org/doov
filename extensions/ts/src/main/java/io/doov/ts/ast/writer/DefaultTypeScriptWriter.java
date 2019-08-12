/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import io.doov.core.dsl.meta.i18n.ResourceProvider;

public class DefaultTypeScriptWriter implements TypeScriptWriter {

    private final List<Import> imports;
    private final Locale locale;
    private final OutputStream os;
    private final ResourceProvider resources;

    public DefaultTypeScriptWriter(Locale locale, OutputStream os, ResourceProvider resources) {
        this.locale = locale;
        this.os = os;
        this.resources = resources;
        this.imports = new ArrayList<>();
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void write(String value) {
        try {
            os.write(value.getBytes(UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Import> getImports() {
        return imports;
    }

    @Override
    public OutputStream getOutput() {
        return os;
    }

    public static class DefaultImport implements Import {

        private final Map<String, String> symbols;
        private final String from;

        public DefaultImport(String symbol, String as, String from) {
            this.from = from;
            Map<String, String> symbolMap = this.symbols = new HashMap<>();
            symbolMap.put(symbol, as);
        }

        public DefaultImport(String symbol, String from) {
            this.from = from;
            Map<String, String> symbolMap = this.symbols = new HashMap<>();
            symbolMap.put(symbol, null);
        }

        @Override
        public Map<String, String> symbols() {
            return symbols;
        }

        @Override
        public String from() {
            return from;
        }
    }

}
