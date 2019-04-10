/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.iterable;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.Value;

public class IterableContains<T, C extends Iterable<T>> extends Apply2<C,T,Boolean> {

    public IterableContains(Value<C> lhs, Value<T> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
