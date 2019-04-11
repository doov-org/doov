/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import static io.doov.core.dsl.utils.JsonGrammar.*;

import io.doov.core.dsl.utils.JsonGrammar;

public abstract class Apply3<I,J,K,O> extends Application<O> {

    public final Value<I> input1;
    public final Value<J> input2;
    public final Value<K> input3;

    public Apply3(Class<O> output, Value<I> input1, Value<J> input2, Value<K> input3) {
        super(output);
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + input1.toString() + ", " + input2.toString() + ", " + input3.toString() + ")";
    }

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("meta", new JString("TERNARY")),
                new JBind("tag", new JString(this.getClass().getSimpleName())),
                new JBind("input", input1.jsonNode()),
                new JBind("input", input2.jsonNode()),
                new JBind("input", input3.jsonNode())
        );
    }
}
