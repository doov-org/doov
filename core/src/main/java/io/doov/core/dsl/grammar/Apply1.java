/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

public abstract class Apply1<I,O> extends Value<O> {

    public final Value<I> input;

    public Apply1(Class<O> output, Value<I> input) {
        super(output);
        this.input = input;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + input.toString() + ")";
    }
}
