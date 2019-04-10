/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import java.util.function.Predicate;

import io.doov.core.dsl.grammar.Value;

public class PredicateValue<T> extends Value<Boolean> {

    public final Predicate<T> predicate;

    public PredicateValue(Predicate<T> predicate) {
        super(Boolean.class);
        this.predicate = predicate;
    }
}
