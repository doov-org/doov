/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.field.LocalTimeFieldInfo;

public class LocalTimeCondition extends TemporalCondition<LocalTimeFieldInfo, LocalTime> {

    public LocalTimeCondition(LocalTimeFieldInfo field) {
        super(field);
    }

    @Override
    Function<LocalTime, LocalTime> minusFunction(int value, TemporalUnit unit) {
        return d -> d.minus(value, unit);
    }

    @Override
    Function<LocalTime, LocalTime> plusFunction(int value, TemporalUnit unit) {
        return d -> d.plus(value, unit);
    }

    @Override
    BiFunction<LocalTime, LocalTime, Boolean> afterFunction() {
        return LocalTime::isAfter;
    }

    @Override
    BiFunction<LocalTime, LocalTime, Boolean> beforeFunction() {
        return LocalTime::isBefore;
    }

    @Override
    BiFunction<LocalTime, LocalTime, Long> betweenFunction(ChronoUnit unit) {
        return unit::between;
    }

}
