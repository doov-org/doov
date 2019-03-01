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

import java.time.LocalTime;
import java.time.temporal.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface LocalTimeOperators extends TemporalOperators<LocalTime> {

    @Override
    default Function<LocalTime, LocalTime> minusFunction(int value, TemporalUnit unit) {
        return time -> time.minus(value, unit);
    }

    @Override
    default Function<LocalTime, LocalTime> plusFunction(int value, TemporalUnit unit) {
        return time -> time.plus(value, unit);
    }

    @Override
    default Function<LocalTime, LocalTime> withFunction(TemporalAdjuster ajuster) {
        return time -> time.with(ajuster);
    }

    @Override
    default BiFunction<LocalTime, LocalTime, Boolean> afterFunction() {
        return LocalTime::isAfter;
    }

    @Override
    default BiFunction<LocalTime, LocalTime, Boolean> afterOrEqualsFunction() {
        return (time1, time2) -> time1.isAfter(time2) || time1.equals(time2);
    }

    @Override
    default BiFunction<LocalTime, LocalTime, Boolean> beforeFunction() {
        return LocalTime::isBefore;
    }

    @Override
    default BiFunction<LocalTime, LocalTime, Boolean> beforeOrEqualsFunction() {
        return (time1, time2) -> time1.isBefore(time2) || time1.equals(time2);
    }

    @Override
    default BiFunction<LocalTime, LocalTime, Long> betweenFunction(ChronoUnit unit) {
        return unit::between;
    }
}
