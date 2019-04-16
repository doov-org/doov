/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.numeric;

import java.util.List;

import io.doov.core.dsl.grammar.ApplyN;
import io.doov.core.dsl.grammar.ASTNode;

public class Min<N extends Number> extends ApplyN<N,N> {

    public Min(Class<N> output, List<ASTNode<N>> inputs) {
        super(output, inputs);
    }
}
