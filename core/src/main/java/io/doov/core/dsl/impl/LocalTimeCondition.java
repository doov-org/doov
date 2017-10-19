/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.function.BiFunction;

import io.doov.core.dsl.field.LocalTimeFieldInfo;

public class LocalTimeCondition extends TemporalCondition<LocalTimeFieldInfo, LocalTime> {

    public LocalTimeCondition(LocalTimeFieldInfo field) {
        super(field);
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
