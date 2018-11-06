/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.core.dsl.impl;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

/**
 * Implements {@link Float} functions for the numeric conditions.
 */
public class FloatCondition extends NumericCondition<Float> {

    public FloatCondition(DslField<Float> field) {
        super(field);
    }

    public FloatCondition(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<Float>> value) {
        super(metadata, value);
    }

    @Override
    protected NumericCondition<Float> numericCondition(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<Float>> value) {
        return new FloatCondition(metadata, value);
    }

    @Override
    BiFunction<Float, Float, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    BiFunction<Float, Float, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    BiFunction<Float, Float, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    BiFunction<Float, Float, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

    @Override
    BinaryOperator<Float> minFunction() {
        return Float::min;
    }

    @Override
    BinaryOperator<Float> sumFunction() {
        return Float::sum;
    }

    @Override
    BiFunction<Float, Integer, Float> timesFunction() {
        return (l, r) -> l * r;
    }

    @Override
    Float identity() {
        return 0f;
    }

}
