/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.BiFunction;

import io.doov.core.dsl.field.LocalDateTimeFieldInfo;

public class LocalDateTimeCondition extends TemporalCondition<LocalDateTimeFieldInfo, LocalDateTime> {

    public LocalDateTimeCondition(LocalDateTimeFieldInfo field) {
        super(field);
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
