/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import io.doov.core.dsl.DslField;

public class DefaultFieldNameProvider implements FieldNameProvider {

    @Override
    public String getFieldName(DslField<?> field) {
        return field.id().code();
    }
}
