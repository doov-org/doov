/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

public interface TypeScriptWriter {

    Locale getLocale();

    void write(String value);

    List<Import> getImports();

    OutputStream getOutput();

}
