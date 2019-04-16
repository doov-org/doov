/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.iterable;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.ASTNode;

public class IterableHasSize<T, C extends Iterable<T>> extends Apply2<C,Integer,Boolean> {

    public IterableHasSize(ASTNode<C> lhs, ASTNode<Integer> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
