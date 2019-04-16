/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import io.doov.core.dsl.grammar.ASTNode;

public class FunctionOut<T> extends ASTNode<T> {

    @Override
    public String toString() {
        return "FUNCTION OUT";
    }

    @Override
    public JNode json() {
        return new JString("FUNCTION OUT");
    }
}
