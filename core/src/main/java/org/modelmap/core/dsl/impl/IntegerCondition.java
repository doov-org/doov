/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;
import java.util.function.Function;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.IntegerFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class IntegerCondition extends NumericCondition<IntegerFieldInfo, Integer> {

    public IntegerCondition(IntegerFieldInfo field) {
        super(field);
    }

    public IntegerCondition(FieldMetadata metadata, Function<FieldModel, Optional<Integer>> value) {
        super(metadata, value);
    }

    @Override
    public Function<Integer, Boolean> lesserThanFunction(Integer value) {
        return i -> i < value;
    }

    @Override
    public Function<Integer, Boolean> lesserOrEqualsFunction(Integer value) {
        return i -> i <= value;
    }

    @Override
    public Function<Integer, Boolean> greaterThanFunction(Integer value) {
        return i -> i > value;
    }

    @Override
    public Function<Integer, Boolean> greaterOrEqualsFunction(Integer value) {
        return i -> i >= value;
    }

    @Override
    public Function<Integer, Boolean> betweenFunction(Integer minIncluded, Integer maxExcluded) {
        return i -> i >= minIncluded && i < maxExcluded;
    }

}
