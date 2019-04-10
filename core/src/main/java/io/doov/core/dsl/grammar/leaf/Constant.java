/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import io.doov.core.dsl.grammar.Value;

public class Constant<T> extends Value<T> {

    public final T value;

    public Constant(T value) {
        super((Class<T>)value.getClass());
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Constant(%s)", value.toString());
    }
}
