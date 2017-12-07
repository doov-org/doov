/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public class IterableCondition<T, C extends Iterable<T>> extends DefaultCondition<C> {

    public IterableCondition(DslField field) {
        super(field);
    }

    public IterableCondition(DslField field, FieldMetadata metadata, BiFunction<DslModel, Context, Optional<C>> value) {
        super(field, metadata, value);
    }

    public StepCondition contains(T value) {
        return predicate(containsMetadata(field, value),
                collection -> stream(collection.spliterator(), false)
                        .anyMatch(value::equals));
    }

    @SafeVarargs
    public final StepCondition containsAll(T... values) {
        return predicate(containsMetadata(field, (Object[]) values),
                iterable -> stream(iterable.spliterator(), false)
                        .collect(toSet()).containsAll(asList(values)));
    }

    public StepCondition isEmpty() {
        return predicate(isEmptyMetadata(field), iterable -> !iterable.iterator().hasNext());
    }

    public StepCondition isNotEmpty() {
        return predicate(isNotEmptyMetadata(field), iterable -> iterable.iterator().hasNext());
    }

    public StepCondition hasSize(int size) {
        return predicate(hasSizeMetadata(field, size),
                iterable -> stream(iterable.spliterator(), false).count() == size);
    }

    public StepCondition hasNotSize(int size) {
        return predicate(hasNotSizeMetadata(field, size),
                iterable -> stream(iterable.spliterator(), false).count() != size);
    }

}
