/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.test;

import io.doov.core.dsl.DslField;

public class FieldAssertionSpec {
    private final DslField<?> field;
    private final String fieldName;
    private final String expected;

    public FieldAssertionSpec(DslField<?> field, String fieldName, String expected) {
        this.field = field;
        this.fieldName = fieldName;
        this.expected = expected;
    }

    public DslField<?> getField() {
        return field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getExpected() {
        return expected;
    }
}
