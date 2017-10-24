/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.*;
import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.CollectionFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public class CollectionCondition<T, C extends Collection<T>>
                extends DefaultCondition<CollectionFieldInfo<T, C>, C> {

    public CollectionCondition(CollectionFieldInfo<T, C> field) {
        super(field);
    }

    public CollectionCondition(FieldMetadata metadata, Function<FieldModel, Optional<C>> value) {
        super(metadata, value);
    }

    public StepCondition contains(T value) {
        return predicate(containsMetadata(field, value), collection -> collection.contains(value));
    }

    @SafeVarargs
    public final StepCondition containsAll(T... values) {
        return predicate(containsMetadata(field, values), collection -> collection.containsAll(asList(values)));
    }

    public StepCondition isEmpty() {
        return predicate(isEmptyMetadata(field), collection -> collection.isEmpty());
    }

    public StepCondition isNotEmpty() {
        return predicate(isNotEmptyMetadata(field), collection -> !collection.isEmpty());
    }

    public StepCondition hasSize(int size) {
        return predicate(hasSizeMetadata(field, size), collection -> collection.size() == size);
    }

    public StepCondition hasNotSize(int size) {
        return predicate(hasNotSizeMetadata(field, size), collection -> collection.size() != size);
    }

}
