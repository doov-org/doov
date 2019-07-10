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

public interface DoubleOperators extends NumericOperators<Double> {
    @Override
    default BiFunction<Double, Double, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    default BiFunction<Double, Double, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    default BiFunction<Double, Double, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    default BiFunction<Double, Double, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

    @Override
    default BinaryOperator<Double> minFunction() {
        return Double::min;
    }

    @Override
    default BinaryOperator<Double> maxFunction() {
        return Double::max;
    }

    @Override
    default BinaryOperator<Double> sumFunction() {
        return Double::sum;
    }

    @Override
    default BinaryOperator<Double> minusFunction() {
        return (l, r) -> l - r;
    }

    @Override
    default BiFunction<Double, Integer, Double> timesFunction() {
        return (l, r) -> l * r;
    }

    @Override
    default Double identity() {
        return 0d;
    }
}
