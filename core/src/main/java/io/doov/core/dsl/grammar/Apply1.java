/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import static io.doov.core.dsl.utils.JsonGrammar.JBind;
import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

public abstract class Apply1<I,O> extends Application<O> {

    public final Value<I> input;

    public Apply1(Class<O> output, Value<I> input) {
        super(output);
        this.input = input;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + input.toString() + ")";
    }

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("meta", new JString("UNARY")),
                new JBind("tag", new JString(this.getClass().getSimpleName())),
                new JBind("input", input.jsonNode())
        );
    }
}
