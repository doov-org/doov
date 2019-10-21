/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import java.util.Map;

public interface ImportSpec {

    String from();

    Map<String, String> symbols();

    boolean isStarImport();

    String toImportStatement();
}
