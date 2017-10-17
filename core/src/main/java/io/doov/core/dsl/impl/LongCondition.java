/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.util.Optional;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.LongFieldInfo;
import io.doov.core.dsl.meta.FieldMetadata;

public class LongCondition extends NumericCondition<LongFieldInfo, Long> {

    public LongCondition(LongFieldInfo field) {
        super(field);
    }

    public LongCondition(FieldMetadata metadata, Function<FieldModel, Optional<Long>> value) {
        super(metadata, value);
    }

    @Override
    public Function<Long, Boolean> lesserThanFunction(Long value) {
        return i -> i < value;
    }

    @Override
    public Function<Long, Boolean> lesserOrEqualsFunction(Long value) {
        return i -> i <= value;
    }

    @Override
    public Function<Long, Boolean> greaterThanFunction(Long value) {
        return i -> i > value;
    }

    @Override
    public Function<Long, Boolean> greaterOrEqualsFunction(Long value) {
        return i -> i >= value;
    }

    @Override
    public Function<Long, Boolean> betweenFunction(Long minIncluded, Long maxExcluded) {
        return i -> i >= minIncluded && i < maxExcluded;
    }

}
