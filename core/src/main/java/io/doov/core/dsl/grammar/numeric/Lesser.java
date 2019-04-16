/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.numeric;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.ASTNode;

public class Lesser<N extends Number> extends Apply2<N,N,Boolean> {

    public Lesser(ASTNode<N> lhs, ASTNode<N> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}