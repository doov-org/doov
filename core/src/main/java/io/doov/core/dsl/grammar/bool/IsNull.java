/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.bool;

import io.doov.core.dsl.grammar.Apply1;
import io.doov.core.dsl.grammar.Value;

public class IsNull<T> extends Apply1<T,Boolean> {

    public IsNull(Value<T> input) {
        super(Boolean.class, input);
    }
}
