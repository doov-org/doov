/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import java.io.OutputStream;
import java.util.*;

import io.doov.core.dsl.DslField;

public interface TypeScriptWriter {

    String DOT = ".";
    String COMMA = ",";
    String SPACE = " ";
    String COLUMN = ";";
    String NEW_LINE = "\n";
    String ASSIGN = "=";
    String LEFT_PARENTHESIS = "(";
    String RIGHT_PARENTHESIS = ")";

    Locale getLocale();

    void write(String value);

    void writeNewLine(int indent);

    void writeGlobalDOOV();

    void writeQuote();

    void writeField(FieldSpec fieldSpec);

    Collection<ImportSpec> getImports();

    Collection<FieldSpec> getFields();

    void addField(FieldSpec fieldSpec);

    void addImport(ImportSpec importSpec);

    OutputStream getOutput();

}
