/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.util.Optional;
import java.util.function.BiFunction;
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
    public BiFunction<Long, Long, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    public BiFunction<Long, Long, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    public BiFunction<Long, Long, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    public BiFunction<Long, Long, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

}
