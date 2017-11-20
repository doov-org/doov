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

import java.time.LocalDateTime;
import java.time.temporal.*;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class LocalDateTimeCondition extends TemporalCondition<LocalDateTime> {

    public LocalDateTimeCondition(DslField field) {
        super(field);
    }

    public LocalDateTimeCondition(Metadata metadata, BiFunction<DslModel, Context, Optional<LocalDateTime>> function) {
        super(metadata, function);
    }

    public LocalDateTimeCondition(DslField field, Metadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalDateTime>> value) {
        super(field, metadata, value);
    }

    @Override
    protected TemporalCondition<LocalDateTime> temporalCondition(Metadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalDateTime>> value) {
        return new LocalDateTimeCondition(metadata, value);
    }

    @Override
    protected TemporalCondition<LocalDateTime> temporalCondition(DslField field, Metadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalDateTime>> value) {
        return new LocalDateTimeCondition(field, metadata, value);
    }

    @Override
    protected Function<LocalDateTime, LocalDateTime> minusFunction(int value, TemporalUnit unit) {
        return d -> d.minus(value, unit);
    }

    @Override
    protected Function<LocalDateTime, LocalDateTime> plusFunction(int value, TemporalUnit unit) {
        return d -> d.plus(value, unit);
    }

    @Override
    protected Function<LocalDateTime, LocalDateTime> withFunction(TemporalAdjuster ajuster) {
        return d -> d.with(ajuster);
    }

    @Override
    protected BiFunction<LocalDateTime, LocalDateTime, Boolean> afterFunction() {
        return LocalDateTime::isAfter;
    }

    @Override
    protected BiFunction<LocalDateTime, LocalDateTime, Boolean> beforeFunction() {
        return LocalDateTime::isBefore;
    }

    @Override
    protected BiFunction<LocalDateTime, LocalDateTime, Long> betweenFunction(ChronoUnit unit) {
        return unit::between;
    }

}
