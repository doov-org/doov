/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

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
        return LeafStepCondition.stepCondition(containsMetadata(metadata, value), getFunction(),
                collection -> stream(collection.spliterator(), false).anyMatch(value::equals));
    }

    @SafeVarargs
    public final StepCondition containsAll(T... values) {
        return LeafStepCondition.stepCondition(containsMetadata(metadata, Arrays.asList(values)), getFunction(),
                iterable -> stream(iterable.spliterator(), false).collect(toSet()).containsAll(asList(values)));
    }

    public StepCondition isEmpty() {
        return LeafStepCondition.stepCondition(isEmptyMetadata(metadata), getFunction(),
                iterable -> !iterable.iterator().hasNext());
    }

    public StepCondition isNotEmpty() {
        return LeafStepCondition.stepCondition(isNotEmptyMetadata(metadata), getFunction(),
                iterable -> iterable.iterator().hasNext());
    }

    public StepCondition hasSize(int size) {
        return LeafStepCondition.stepCondition(hasSizeMetadata(metadata, size), getFunction(),
                iterable -> stream(iterable.spliterator(), false).count() == size);
    }

    public StepCondition hasNotSize(int size) {
        return LeafStepCondition.stepCondition(hasNotSizeMetadata(metadata, size), getFunction(),
                iterable -> stream(iterable.spliterator(), false).count() != size);
    }

}
