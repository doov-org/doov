/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.bool;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.ASTNode;

public class Or extends Apply2<Boolean,Boolean,Boolean> {

    public Or(ASTNode<Boolean> lhs, ASTNode<Boolean> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
