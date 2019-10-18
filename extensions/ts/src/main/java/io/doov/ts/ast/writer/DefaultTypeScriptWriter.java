/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import static io.doov.ts.ast.writer.DefaultImportSpec.starImport;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import io.doov.core.dsl.meta.i18n.ResourceProvider;

public class DefaultTypeScriptWriter implements TypeScriptWriter {

    private final Locale locale;
    private final OutputStream os;
    private final ResourceProvider resources;
    private final Set<ImportSpec> imports;
    private final Set<FieldSpec> fields;
    private final int indentSpace = 2;

    public DefaultTypeScriptWriter(Locale locale,
            OutputStream os,
            ResourceProvider resources) {
        this.locale = locale;
        this.os = os;
        this.resources = resources;
        this.imports = new HashSet<>();
        this.fields = new HashSet<>();
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
        addImport(starImport("DOOV", "doov"));
    }

    @Override
    public void writeQuote() {
        write("\'");
    }

    @Override
    public void writeField(FieldSpec fieldSpec) {
        addField(fieldSpec);
        write(fieldSpec.name());
    }

    @Override
    public Set<ImportSpec> getImports() {
        return Collections.unmodifiableSet(imports);
    }

    @Override
    public Set<FieldSpec> getFields() {
        return Collections.unmodifiableSet(fields);
    }

    @Override
    public OutputStream getOutput() {
        return os;
    }

    @Override
    public synchronized void addImport(ImportSpec importSpec) {
        imports.add(importSpec);
    }

    @Override
    public synchronized void addField(FieldSpec fieldSpec) {
        fields.add(fieldSpec);
    }

}
