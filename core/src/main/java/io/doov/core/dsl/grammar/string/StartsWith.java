/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.string;

import io.doov.core.dsl.grammar.Apply2;
import io.doov.core.dsl.grammar.ASTNode;

public class StartsWith extends Apply2<String,String,Boolean> {

    public StartsWith(ASTNode<String> lhs, ASTNode<String> rhs) {
        super(Boolean.class, lhs, rhs);
    }
}
