/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.i18n.ResourceProvider;

public class DefaultTypeScriptWriter implements TypeScriptWriter {

    private final List<ImportSpec> imports;
    private final Locale locale;
    private final OutputStream os;
    private final ResourceProvider resources;
    private final List<FieldSpec> fields;
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
        imports.add(new ImportSpec("*", "DOOV", "doov"));
    }

    @Override
    public void writeQuote() {
        write("\'");
    }

    @Override
    public void writeField(DslField<?> field) {
        FieldSpec spec = new FieldSpec(fieldNameProvider.getFieldName(field), field, field instanceof FieldInfo ?
                (FieldInfo) field : null);
        fields.add(spec);
        try {
            os.write(spec.name().getBytes(UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ImportSpec> getImports() {
        return imports;
    }

    @Override
    public List<FieldSpec> getFields() {
        return fields;
    }

    @Override
    public OutputStream getOutput() {
        return os;
    }

}
