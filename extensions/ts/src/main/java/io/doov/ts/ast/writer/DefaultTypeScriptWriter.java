/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.i18n.ResourceProvider;

public class DefaultTypeScriptWriter implements TypeScriptWriter {

    private final List<Import> imports;
    private final Locale locale;
    private final OutputStream os;
    private final ResourceProvider resources;
    private final List<DslField<?>> fields;
    private final int indentSpace = 2;
    private final FieldNameProvider fieldNameProvider;

    public DefaultTypeScriptWriter(Locale locale, OutputStream os, ResourceProvider resources) {
        this(locale, os, resources, new DefaultFieldNameProvider());
    }

    public DefaultTypeScriptWriter(Locale locale,
            OutputStream os,
            ResourceProvider resources,
            FieldNameProvider fieldNameProvider) {
        this.locale = locale;
        this.os = os;
        this.resources = resources;
        this.imports = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.fieldNameProvider = fieldNameProvider;
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
    public void writeNewLine(int indent) {
        write(NEW_LINE);
        for (int i = 0; i < indent * indentSpace; i++) {
            write(SPACE);
        }
    }

    @Override
    public void writeGlobalDOOV() {
        write("DOOV");
    }

    @Override
    public void writeQuote() {
        write("\'");
    }

    @Override
    public void writeField(DslField<?> field) {
        fields.add(field);
        try {
            os.write(fieldNameProvider.getFieldName(field).getBytes(UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Import> getImports() {
        return imports;
    }

    @Override
    public List<DslField<?>> getFields() {
        return fields;
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
