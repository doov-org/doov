/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import io.doov.core.dsl.grammar.Value;

public class Constant<T> extends Value<T> {

    public final T value;
    public final Class<T> valueClass;

    public Constant(T value) {
        this.value = value;
        this.valueClass = (Class<T>) value.getClass();
    }

    @Override
    public String toString() {
        return String.format("Constant(%s)", value.toString());
    }
}
