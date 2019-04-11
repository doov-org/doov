/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import io.doov.core.dsl.utils.JsonGrammar;

public abstract class Value<T> {

    @Override
    abstract public String toString();

    abstract public JsonGrammar.JNode jsonNode();

    public String json() {
        return jsonNode().toString();
    }
}
