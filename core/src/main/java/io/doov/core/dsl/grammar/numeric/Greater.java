/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.numeric;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.Value;

public class Greater<N extends Number> extends Apply2<N,N,Boolean> {

    public Greater(Value<N> lhs, Value<N> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
