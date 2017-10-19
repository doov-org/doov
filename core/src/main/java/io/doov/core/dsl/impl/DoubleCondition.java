/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.DoubleFieldInfo;
import io.doov.core.dsl.meta.FieldMetadata;

public class DoubleCondition extends NumericCondition<DoubleFieldInfo, Double> {

    public DoubleCondition(DoubleFieldInfo field) {
        super(field);
    }

    public DoubleCondition(FieldMetadata metadata, Function<FieldModel, Optional<Double>> value) {
        super(metadata, value);
    }

    @Override
    public BiFunction<Double, Double, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    public BiFunction<Double, Double, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    public BiFunction<Double, Double, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    public BiFunction<Double, Double, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

}
