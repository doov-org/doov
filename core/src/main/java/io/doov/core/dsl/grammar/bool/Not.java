/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.bool;

import io.doov.core.dsl.grammar.Apply1;
import io.doov.core.dsl.grammar.Value;

public class Not extends Apply1<Boolean,Boolean> {

    public Not(Value<Boolean> input) {
        super(Boolean.class, input);
    }
}
