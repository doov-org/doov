/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class IterableFunction<T, C extends Iterable<T>> extends IterableCondition<T, C> {

    public IterableFunction(DslField<C> field) {
        super(field);
    }

    public IterableFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<C>> value) {
        super(metadata, value);
    }

}
