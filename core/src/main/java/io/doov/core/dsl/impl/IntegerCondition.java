/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.IntegerFieldInfo;
import io.doov.core.dsl.meta.FieldMetadata;

public class IntegerCondition extends NumericCondition<IntegerFieldInfo, Integer> {

    public IntegerCondition(IntegerFieldInfo field) {
        super(field);
    }

    public IntegerCondition(FieldMetadata metadata, Function<FieldModel, Optional<Integer>> value) {
        super(metadata, value);
    }

    @Override
    public BiFunction<Integer, Integer, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    public BiFunction<Integer, Integer, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    public BiFunction<Integer, Integer, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    public BiFunction<Integer, Integer, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

}
