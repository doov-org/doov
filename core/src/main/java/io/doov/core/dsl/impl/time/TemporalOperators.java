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
package io.doov.core.dsl.impl.time;

import java.time.temporal.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface TemporalOperators<N extends Temporal> {
    
    Function<N, N> minusFunction(int value, TemporalUnit unit);

    Function<N, N> plusFunction(int value, TemporalUnit unit);

    Function<N, N> withFunction(TemporalAdjuster ajuster);

    BiFunction<N, N, Boolean> afterFunction();

    BiFunction<N, N, Boolean> afterOrEqualsFunction();

    BiFunction<N, N, Boolean> beforeFunction();

    BiFunction<N, N, Boolean> beforeOrEqualsFunction();

    BiFunction<N, N, Long> betweenFunction(ChronoUnit unit);
}
