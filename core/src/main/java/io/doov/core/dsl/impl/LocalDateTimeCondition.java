/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.field.LocalDateTimeFieldInfo;

public class LocalDateTimeCondition extends TemporalCondition<LocalDateTimeFieldInfo, LocalDateTime> {

    public LocalDateTimeCondition(LocalDateTimeFieldInfo field) {
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
