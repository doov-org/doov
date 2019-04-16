/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.bool;

import java.util.List;

import io.doov.core.dsl.grammar.ApplyN;
import io.doov.core.dsl.grammar.ASTNode;

public class All extends ApplyN<Boolean,Boolean> {

    public All(List<ASTNode<Boolean>> inputs) {
        super(Boolean.class, inputs);
    }
}
