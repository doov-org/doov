/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import io.doov.core.dsl.grammar.Value;

public class FunctionIn<T> extends Value<T> {

    @Override
    public String toString() {
        return "FUNCTION IN";
    }

    @Override
    public JNode jsonNode() {
        return new JString("FUNCTION IN");
    }
}
