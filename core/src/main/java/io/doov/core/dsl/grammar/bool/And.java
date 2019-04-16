/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.bool;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.ASTNode;

public class And extends Apply2<Boolean,Boolean,Boolean> {

    public And(ASTNode<Boolean> lhs, ASTNode<Boolean> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
