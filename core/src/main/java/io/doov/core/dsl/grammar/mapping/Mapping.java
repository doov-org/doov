/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import io.doov.core.dsl.grammar.ASTNode;
import io.doov.core.dsl.utils.JsonGrammar;

public class Mapping<T> extends ASTNode<Void> {
    public final ASTNode<T> input;
    public final ASTNode<T> output;

    public Mapping(ASTNode<T> input, ASTNode<T> output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public String toString() {
        return "Mapping(" + input.toString() + " -> " + output.toString() + ")";
    }

    @Override
    public JsonGrammar.JNode json() {
        return new JsonGrammar.JObject(
                new JsonGrammar.JBind("meta",new JsonGrammar.JString("MAPPING")),
                new JsonGrammar.JBind("input", input.json()),
                new JsonGrammar.JBind("output",output.json())
        );
    }
}
