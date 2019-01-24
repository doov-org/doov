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

public interface NumericOperators<N extends Number> {

    BiFunction<N, N, Boolean> lesserThanFunction();

    BiFunction<N, N, Boolean> lesserOrEqualsFunction();

    BiFunction<N, N, Boolean> greaterThanFunction();

    BiFunction<N, N, Boolean> greaterOrEqualsFunction();

    BinaryOperator<N> minFunction();

    BinaryOperator<N> sumFunction();

    BiFunction<N, Integer, N> timesFunction();

    N identity();
}
