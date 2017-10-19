/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.FloatFieldInfo;
import io.doov.core.dsl.meta.FieldMetadata;

public class FloatCondition extends NumericCondition<FloatFieldInfo, Float> {

    public FloatCondition(FloatFieldInfo field) {
        super(field);
    }

    public FloatCondition(FieldMetadata metadata, Function<FieldModel, Optional<Float>> value) {
        super(metadata, value);
    }

    @Override
    public BiFunction<Float, Float, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    public BiFunction<Float, Float, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    public BiFunction<Float, Float, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    public BiFunction<Float, Float, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

}