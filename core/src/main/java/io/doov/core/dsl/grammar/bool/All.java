/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.bool;

import java.util.List;

import io.doov.core.dsl.grammar.ApplyN;
import io.doov.core.dsl.grammar.Value;

public class All extends ApplyN<Boolean,Boolean> {

    public All(List<Value<Boolean>> inputs) {
        super(Boolean.class, inputs);
    }
}
