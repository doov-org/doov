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

import java.time.LocalTime;
import java.time.temporal.*;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class LocalTimeCondition extends TemporalCondition<LocalTime> {

    public LocalTimeCondition(DslField field) {
        super(field);
    }

    public LocalTimeCondition(DslField field, Metadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalTime>> value) {
        super(field, metadata, value);
    }

    @Override
    protected TemporalCondition<LocalTime> temporalCondition(DslField field, Metadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalTime>> value) {
        return new LocalTimeCondition(field, metadata, value);
    }

    @Override
    protected Function<LocalTime, LocalTime> minusFunction(int value, TemporalUnit unit) {
        return d -> d.minus(value, unit);
    }

    @Override
    protected Function<LocalTime, LocalTime> plusFunction(int value, TemporalUnit unit) {
        return d -> d.plus(value, unit);
    }

    @Override
    protected Function<LocalTime, LocalTime> withFunction(TemporalAdjuster ajuster) {
        return d -> d.with(ajuster);
    }

    @Override
    protected BiFunction<LocalTime, LocalTime, Boolean> afterFunction() {
        return LocalTime::isAfter;
    }

    @Override
    protected BiFunction<LocalTime, LocalTime, Boolean> beforeFunction() {
        return LocalTime::isBefore;
    }

    @Override
    protected BiFunction<LocalTime, LocalTime, Long> betweenFunction(ChronoUnit unit) {
        return unit::between;
    }

}
