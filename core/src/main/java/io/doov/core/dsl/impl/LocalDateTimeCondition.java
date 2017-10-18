/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

import io.doov.core.dsl.field.LocalDateTimeFieldInfo;

public class LocalDateTimeCondition extends TemporalCondition<LocalDateTimeFieldInfo, LocalDateTime> {

    public LocalDateTimeCondition(LocalDateTimeFieldInfo field) {
        super(field);
    }

    @Override
    Function<LocalDateTime, Boolean> afterFunction(LocalDateTime value) {
        return v -> v.isAfter(value);
    }

    @Override
    Function<LocalDateTime, Boolean> beforeFunction(LocalDateTime value) {
        return v -> v.isBefore(value);
    }

    @Override
    Function<LocalDateTime, Long> betweenFunction(ChronoUnit unit, LocalDateTime value) {
        return v -> unit.between(v, value);
    }

}
