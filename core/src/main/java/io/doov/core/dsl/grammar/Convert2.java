/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import java.util.function.BiFunction;

import io.doov.core.dsl.lang.BiTypeConverter;

public class Convert2<I,J,O> extends Value<O> {

    public final Value<I> lhs;
    public final Value<J> rhs;
    public final BiTypeConverter<I,J,O> process;

    public Convert2(Value<I> lhs, Value<J> rhs, BiTypeConverter<I,J,O> process) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.process = process;
    }
}
