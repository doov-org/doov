/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.string;

import io.doov.core.dsl.grammar.Apply1;
import io.doov.core.dsl.grammar.ASTNode;

public class StringLength extends Apply1<String,Integer> {

    public StringLength(ASTNode<String> input) {
        super(Integer.class, input);
    }
}
