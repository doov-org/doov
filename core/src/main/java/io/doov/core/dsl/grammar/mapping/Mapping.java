/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import io.doov.core.dsl.grammar.Value;

public class Mapping<T> extends Value<Void> {
    public final Value<T> input;
    public final Value<T> output;

    public Mapping(Value<T> input, Value<T> output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public String toString() {
        return "Mapping(" + input.toString() + " -> " + output.toString() + ")";
    }
}
