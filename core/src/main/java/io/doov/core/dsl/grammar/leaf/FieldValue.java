/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.grammar.Value;

public class FieldValue<T, F extends DslField<T> & FieldInfo> extends Value<T> {

    public final F field;

    public FieldValue(F field) {
        super((Class<T>)field.type());
        this.field = field;
    }

    @Override
    public String toString() {
        return "FieldValue(" + field.readable() + ":" + output.getSimpleName() + ")";
    }
}
