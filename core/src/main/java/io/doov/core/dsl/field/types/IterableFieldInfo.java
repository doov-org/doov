/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field.types;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.field.BaseFieldInfo;
import io.doov.core.dsl.field.DelegatingFieldInfoImpl;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.IterableCondition;
import io.doov.core.dsl.lang.StepCondition;

public class IterableFieldInfo<T, C extends Iterable<T>> extends DelegatingFieldInfoImpl implements BaseFieldInfo<C> {

    public IterableFieldInfo(FieldInfo fieldInfo) {
        super(fieldInfo);
    }

    @Override
    public DefaultCondition<C> getDefaultCondition() {
        return new DefaultCondition<>(this);
    }

    public StepCondition contains(T value) {
        return new IterableCondition<>(this).contains(value);
    }

    @SafeVarargs
    public final StepCondition containsAll(T... values) {
        return new IterableCondition<>(this).containsAll(values);
    }

    public StepCondition isEmpty() {
        return new IterableCondition<>(this).isEmpty();
    }

    public StepCondition isNotEmpty() {
        return new IterableCondition<>(this).isNotEmpty();
    }

    public StepCondition hasSize(int size) {
        return new IterableCondition<>(this).hasSize(size);
    }

    public StepCondition hasNotSize(int size) {
        return new IterableCondition<>(this).hasNotSize(size);
    }

}
