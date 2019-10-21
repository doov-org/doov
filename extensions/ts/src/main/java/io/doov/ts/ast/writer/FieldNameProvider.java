/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import io.doov.core.dsl.DslField;

public interface FieldNameProvider {

    String getFieldName(DslField<?> field);
}
