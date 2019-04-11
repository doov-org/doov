/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.bool;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.Value;

public class Equals<T> extends Apply2<T,T,Boolean> {

    public Equals(Value<T> lhs, Value<T> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
