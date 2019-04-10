/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import io.doov.core.dsl.lang.TypeConverter;

public class Convert1<I,O> extends Value<O> {

    public final Value<I> input;
    public final TypeConverter<I,O> process;

    public Convert1(Value<I> input, TypeConverter<I,O> process) {
        this.input = input;
        this.process = process;
    }
}
