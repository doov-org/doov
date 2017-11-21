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

import java.time.LocalDate;
import java.time.temporal.*;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class LocalDateCondition extends TemporalCondition<LocalDate> {

    public LocalDateCondition(DslField field) {
        super(field);
    }

    public LocalDateCondition(DslField field, Metadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalDate>> value) {
        super(field, metadata, value);
    }

    @Override
    protected TemporalCondition<LocalDate> temporalCondition(DslField field, Metadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalDate>> value) {
        return new LocalDateCondition(field, metadata, value);
    }

    @Override
    protected Function<LocalDate, LocalDate> minusFunction(int value, TemporalUnit unit) {
        return d -> d.minus(value, unit);
    }

    @Override
    protected Function<LocalDate, LocalDate> plusFunction(int value, TemporalUnit unit) {
        return d -> d.plus(value, unit);
    }

    @Override
    protected Function<LocalDate, LocalDate> withFunction(TemporalAdjuster ajuster) {
        return d -> d.with(ajuster);
    }

    @Override
    protected BiFunction<LocalDate, LocalDate, Boolean> afterFunction() {
        return LocalDate::isAfter;
    }

    @Override
    protected BiFunction<LocalDate, LocalDate, Boolean> beforeFunction() {
        return LocalDate::isBefore;
    }

    @Override
    protected BiFunction<LocalDate, LocalDate, Long> betweenFunction(ChronoUnit unit) {
        return unit::between;
    }

}
