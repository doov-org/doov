/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.numeric;

import java.util.List;

import io.doov.core.dsl.grammar.*;

public class Min<N extends Number> extends ApplyN<N,N> {

    public Min(Class<N> output, List<Value<N>> inputs) {
        super(output, inputs);
    }
}
