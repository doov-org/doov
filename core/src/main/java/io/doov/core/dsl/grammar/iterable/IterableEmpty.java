/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.iterable;

import io.doov.core.dsl.grammar.Apply1;
import io.doov.core.dsl.grammar.Value;

public class IterableEmpty<T, C extends Iterable<T>> extends Apply1<C,Boolean> {

    public IterableEmpty(Value<C> input) {
        super(Boolean.class, input);
    }
}
