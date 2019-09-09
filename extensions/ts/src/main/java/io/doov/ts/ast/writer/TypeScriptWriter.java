/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import io.doov.core.dsl.DslField;

public interface TypeScriptWriter {

    String DOT = ".";
    String COMMA = ",";
    String SPACE = " ";
    String COLUMN = ";";
    String ASSIGN = "=";
    String LEFT_PARENTHESIS = "(";
    String RIGHT_PARENTHESIS = ")";

    Locale getLocale();

    void write(String value);

    void writeGlobalDOOV();

    void writeQuote();

    void writeField(DslField<?> field);

    List<Import> getImports();

    List<DslField<?>> getFields();

    OutputStream getOutput();

}
