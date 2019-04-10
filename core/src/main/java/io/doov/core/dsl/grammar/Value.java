/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

public abstract class Value<T> {
    public final Class<T> output;

    public Value(Class<T> output) {
        this.output = output;
    }
}
