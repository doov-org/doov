/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.util.Optional;
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
    public Function<Float, Boolean> lesserThanFunction(Float value) {
        return i -> i < value;
    }

    @Override
    public Function<Float, Boolean> lesserOrEqualsFunction(Float value) {
        return i -> i <= value;
    }

    @Override
    public Function<Float, Boolean> greaterThanFunction(Float value) {
        return i -> i > value;
    }

    @Override
    public Function<Float, Boolean> greaterOrEqualsFunction(Float value) {
        return i -> i >= value;
    }

    @Override
    public Function<Float, Boolean> betweenFunction(Float minIncluded, Float maxExcluded) {
        return i -> i >= minIncluded && i < maxExcluded;
    }

}