/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;
import java.util.function.Function;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.DoubleFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class DoubleCondition extends NumericCondition<DoubleFieldInfo, Double> {

    public DoubleCondition(DoubleFieldInfo field) {
        super(field);
    }

    public DoubleCondition(FieldMetadata metadata, Function<FieldModel, Optional<Double>> value) {
        super(metadata, value);
    }

    @Override
    public Function<Double, Boolean> lesserThanFunction(Double value) {
        return i -> i < value;
    }

    @Override
    public Function<Double, Boolean> lesserOrEqualsFunction(Double value) {
        return i -> i <= value;
    }

    @Override
    public Function<Double, Boolean> greaterThanFunction(Double value) {
        return i -> i > value;
    }

    @Override
    public Function<Double, Boolean> greaterOrEqualsFunction(Double value) {
        return i -> i >= value;
    }

    @Override
    public Function<Double, Boolean> betweenFunction(Double minIncluded, Double maxExcluded) {
        return i -> i >= minIncluded && i < maxExcluded;
    }

}
