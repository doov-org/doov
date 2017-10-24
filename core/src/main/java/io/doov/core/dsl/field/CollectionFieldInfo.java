/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import java.util.Collection;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.CollectionCondition;
import io.doov.core.dsl.lang.StepCondition;

public class CollectionFieldInfo<T, C extends Collection<T>> extends DefaultFieldInfo<C> {

    CollectionFieldInfo(FieldId fieldId, String readable, Class<?> type, Class<?>[] genericTypes, FieldId[] siblings) {
        super(fieldId, readable, type, genericTypes, siblings);
    }

    public StepCondition contains(T value) {
        return new CollectionCondition<>(this).contains(value);
    }

    @SafeVarargs
    public final StepCondition containsAll(T... values) {
        return new CollectionCondition<>(this).containsAll(values);
    }

    public StepCondition isEmpty() {
        return new CollectionCondition<>(this).isEmpty();
    }

    public StepCondition isNotEmpty() {
        return new CollectionCondition<>(this).isNotEmpty();
    }

    public StepCondition hasSize(int size) {
        return new CollectionCondition<>(this).hasSize(size);
    }

    public StepCondition hasNotSize(int size) {
        return new CollectionCondition<>(this).hasNotSize(size);
    }

}
