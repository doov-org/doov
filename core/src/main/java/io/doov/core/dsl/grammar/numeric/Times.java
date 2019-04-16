/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.numeric;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.ASTNode;

public class Times<N extends Number> extends Apply2<N,Integer,N> {

    public Times(Class<N> output, ASTNode<N> lhs, ASTNode<Integer> rhs) {
        super(output, lhs, rhs);
    }
}
