/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import static io.doov.core.dsl.utils.JsonGrammar.JBind;
import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import java.util.function.Predicate;

import io.doov.core.dsl.grammar.Application;

public class PredicateValue<T> extends Application<Boolean> {

    public final Predicate<T> predicate;

    public PredicateValue(Predicate<T> predicate) {
        super(Boolean.class);
        this.predicate = predicate;
    }

    @Override
    public String toString() {
        return predicate.toString();
    }

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("meta", new JString("PREDICATE")),
                new JBind("data", new JString(predicate.toString()))
        );
    }
}
