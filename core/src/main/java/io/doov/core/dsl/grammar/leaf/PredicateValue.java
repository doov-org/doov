/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import java.util.function.Predicate;

import io.doov.core.dsl.grammar.Application;

public class PredicateValue<T> extends Application<Boolean> {

    public final Predicate<T> predicate;

    public PredicateValue(Predicate<T> predicate) {
        super(Boolean.class);
        this.predicate = predicate;
    }
}
