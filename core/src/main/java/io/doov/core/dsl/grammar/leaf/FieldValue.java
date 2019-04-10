/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.grammar.Value;

public class FieldValue<T> extends Value<T> {

    public final DslField<T> field;

    public FieldValue(DslField<T> field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "FieldValue(" + field.readable() + ":" + field.type().getSimpleName() + ")";
    }
}
