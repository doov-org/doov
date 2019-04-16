/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.bool;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.ASTNode;

public class Equals<T> extends Apply2<T,T,Boolean> {

    public Equals(ASTNode<T> lhs, ASTNode<T> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
