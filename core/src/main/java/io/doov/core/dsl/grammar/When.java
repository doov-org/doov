/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import io.doov.core.dsl.utils.JsonGrammar.JBind;

public class When extends Value<Boolean> {

    public final Value<Boolean> predicate;

    public When(Value<Boolean> predicate) {
        this.predicate = predicate;
    }

    @Override
    public String toString() {
        return "When(" + predicate.toString() + ")";
    }

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("meta", new JString("When")),
                new JBind("data", predicate.jsonNode())
        );
    }

}
