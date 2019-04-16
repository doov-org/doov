/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import io.doov.core.dsl.utils.JsonGrammar.JBind;

public class When extends ASTNode<Boolean> {

    public final ASTNode<Boolean> predicate;

    public When(ASTNode<Boolean> predicate) {
        this.predicate = predicate;
    }

    @Override
    public String toString() {
        return "When(" + predicate.toString() + ")";
    }

    @Override
    public JNode json() {
        return new JObject(
                new JBind("meta", new JString("When")),
                new JBind("data", predicate.json())
        );
    }

}
