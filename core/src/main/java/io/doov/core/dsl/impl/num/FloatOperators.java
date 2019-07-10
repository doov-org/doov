/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl.impl.num;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public interface FloatOperators extends NumericOperators<Float> {

    @Override
    default BiFunction<Float, Float, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    default BiFunction<Float, Float, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    default BiFunction<Float, Float, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    default BiFunction<Float, Float, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

    @Override
    default BinaryOperator<Float> minFunction() {
        return Float::min;
    }

    @Override
    default BinaryOperator<Float> maxFunction() {
        return Float::max;
    }

    @Override
    default BinaryOperator<Float> sumFunction() {
        return Float::sum;
    }

    @Override
    default BinaryOperator<Float> minusFunction() {
        return (l, r) -> l - r;
    }

    @Override
    default BiFunction<Float, Integer, Float> timesFunction() {
        return (l, r) -> l * r;
    }

    @Override
    default Float identity() {
        return 0f;
    }
}
