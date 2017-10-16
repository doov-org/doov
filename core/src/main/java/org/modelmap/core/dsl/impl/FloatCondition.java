/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;
import java.util.function.Function;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.FloatFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

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