/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.string;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.Value;

public class EndsWith extends Apply2<String,String,Boolean> {

    public EndsWith(Value<String> lhs, Value<String> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
