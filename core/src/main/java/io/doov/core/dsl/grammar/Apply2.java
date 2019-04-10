/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

public abstract class Apply2<I,J,O> extends Value<O> {

    public final Value<I> lhs;
    public final Value<J> rhs;

    public Apply2(Class<O> output, Value<I> lhs, Value<J> rhs) {
        super(output);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + lhs.toString() + ", " + rhs.toString() + ")";
    }
}
