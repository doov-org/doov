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
package io.doov.core.dsl.impl;

import java.time.LocalDateTime;
import java.time.temporal.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.SimpleFieldId;

public class LocalDateTimeCondition extends TemporalCondition<LocalDateTime> {

    public LocalDateTimeCondition(SimpleFieldId<LocalDateTime> field) {
        super(field);
    }

    @Override
    Function<LocalDateTime, LocalDateTime> minusFunction(int value, TemporalUnit unit) {
        return d -> d.minus(value, unit);
    }

    @Override
    Function<LocalDateTime, LocalDateTime> plusFunction(int value, TemporalUnit unit) {
        return d -> d.plus(value, unit);
    }

    @Override
    Function<LocalDateTime, LocalDateTime> withFunction(TemporalAdjuster ajuster) {
        return d -> d.with(ajuster);
    }

    @Override
    BiFunction<LocalDateTime, LocalDateTime, Boolean> afterFunction() {
        return LocalDateTime::isAfter;
    }

    @Override
    BiFunction<LocalDateTime, LocalDateTime, Boolean> beforeFunction() {
        return LocalDateTime::isBefore;
    }

    @Override
    BiFunction<LocalDateTime, LocalDateTime, Long> betweenFunction(ChronoUnit unit) {
        return unit::between;
    }

}
