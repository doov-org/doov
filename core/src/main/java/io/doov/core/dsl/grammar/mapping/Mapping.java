/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import io.doov.core.dsl.grammar.Value;
import io.doov.core.dsl.utils.JsonGrammar;

public class Mapping<T> extends Value<Void> {
    public final Value<T> input;
    public final Value<T> output;

    public Mapping(Value<T> input, Value<T> output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public String toString() {
        return "Mapping(" + input.toString() + " -> " + output.toString() + ")";
    }

    @Override
    public JsonGrammar.JNode jsonNode() {
        return new JsonGrammar.JObject(
                new JsonGrammar.JBind("meta",new JsonGrammar.JString("MAPPING")),
                new JsonGrammar.JBind("input", input.jsonNode()),
                new JsonGrammar.JBind("output",output.jsonNode())
        );
    }
}
