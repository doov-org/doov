/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.bool;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.Value;

public class Or extends Apply2<Boolean,Boolean,Boolean> {

    public Or(Value<Boolean> lhs, Value<Boolean> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
