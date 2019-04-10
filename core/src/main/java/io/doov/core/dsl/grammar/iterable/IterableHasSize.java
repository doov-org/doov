/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.iterable;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.Value;

public class IterableHasSize<T, C extends Iterable<T>> extends Apply2<C,Integer,Boolean> {

    public IterableHasSize(Value<C> lhs, Value<Integer> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
