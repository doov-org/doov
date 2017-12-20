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
import io.doov.core.dsl.meta.PredicateMetadata;

public class IntegerCondition extends NumericCondition<Integer> {

    public IntegerCondition(DslField field) {
        super(field);
    }

    public IntegerCondition(DslField field, PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<Integer>> value) {
        super(field, metadata, value);
    }

    public IntegerCondition(NumericCondition<Long> condition) {
        super(condition.field, condition.metadata,
                (model, context) -> condition.function.apply(model, context).map(Long::intValue));
    }

    @Override
    NumericCondition<Integer> numericCondition(DslField field, PredicateMetadata metadata,
            BiFunction<DslModel, Context, Optional<Integer>> value) {
        return new IntegerCondition(field, metadata, value);
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

    @Override
    BinaryOperator<Integer> minFunction() {
        return Integer::min;
    }

    @Override
    BinaryOperator<Integer> sumFunction() {
        return Integer::sum;
    }

    @Override
    BiFunction<Integer, Integer, Integer> timesFunction() {
        return (l, r) -> l * r;
    }

    @Override
    Integer identity() {
        return 0;
    }

}
