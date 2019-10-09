/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import java.util.Map;

public interface Import {

    Map<String, String> symbols();

    String from();
}
