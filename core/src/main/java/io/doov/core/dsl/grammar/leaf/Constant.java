/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import static io.doov.core.dsl.utils.JsonGrammar.JBind;
import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import io.doov.core.dsl.grammar.ASTNode;

public class Constant<T> extends ASTNode<T> {

    public final T value;
    public final Class<T> valueClass;

    public Constant(T value) {
        this.value = value;
        this.valueClass = (Class<T>) value.getClass();
    }

    @Override
    public String toString() {
        return String.format("Constant(%s)", value.toString());
    }

    @Override
    public JNode json() {
        return new JObject(
                new JBind("meta", new JString("CONSTANT")),
                new JBind("class", new JString(valueClass.toString())),
                new JBind("data", new JString(value.toString()))
        );
    }
}
