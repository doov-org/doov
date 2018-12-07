/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.impl.LeafStepCondition.predicate;
import static io.doov.core.dsl.meta.function.IterableFunctionMetadata.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class IterableCondition<T, C extends Iterable<T>> extends DefaultCondition<C> {

    public IterableCondition(DslField<C> field) {
        super(field);
    }

    public IterableCondition(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<C>> value) {
        super(metadata, value);
    }

    public StepCondition contains(T value) {
        return predicate(this, containsMetadata(metadata, value),
                collection -> stream(collection.spliterator(), false)
                        .anyMatch(value::equals));
    }

    @SafeVarargs
    public final StepCondition containsAll(T... values) {
        return predicate(this, containsMetadata(metadata, Arrays.asList(values)),
                iterable -> stream(iterable.spliterator(), false)
                        .collect(toSet()).containsAll(asList(values)));
    }

    public StepCondition isEmpty() {
        return predicate(this, isEmptyMetadata(metadata), iterable -> !iterable.iterator().hasNext());
    }

    public StepCondition isNotEmpty() {
        return predicate(this, isNotEmptyMetadata(metadata),
                iterable -> iterable.iterator().hasNext());
    }

    public StepCondition hasSize(int size) {
        return predicate(this, hasSizeMetadata(metadata, size),
                iterable -> stream(iterable.spliterator(), false).count() == size);
    }

    public StepCondition hasNotSize(int size) {
        return predicate(this, hasNotSizeMetadata(metadata, size),
                iterable -> stream(iterable.spliterator(), false).count() != size);
    }

}
