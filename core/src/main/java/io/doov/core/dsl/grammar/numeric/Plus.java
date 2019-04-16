/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.numeric;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.ASTNode;

public class Plus<N extends Number> extends Apply2<N,N,N> {

    public Plus(Class<N> output, ASTNode<N> lhs, ASTNode<N> rhs) {
        super(output, lhs, rhs);
    }
}