/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.numeric;

import java.util.List;

import io.doov.core.dsl.grammar.ApplyN;
import io.doov.core.dsl.grammar.ASTNode;

public class Count<T> extends ApplyN<T,Integer> {

    public Count(List<ASTNode<T>> inputs) {
        super(Integer.class,inputs);
    }
}
