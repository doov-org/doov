/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.temporal;

import java.time.temporal.Temporal;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.Value;

public class Before<N extends Temporal> extends Apply2<N,N,Boolean> {

    public Before(Value<N> lhs, Value<N> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
