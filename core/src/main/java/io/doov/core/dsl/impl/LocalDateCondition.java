/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.BiFunction;

import io.doov.core.dsl.field.LocalDateFieldInfo;

public class LocalDateCondition extends TemporalCondition<LocalDateFieldInfo, LocalDate> {

    public LocalDateCondition(LocalDateFieldInfo field) {
        super(field);
    }

    @Override
    BiFunction<LocalDate, LocalDate, Boolean> afterFunction() {
        return LocalDate::isAfter;
    }

    @Override
    BiFunction<LocalDate, LocalDate, Boolean> beforeFunction() {
        return LocalDate::isBefore;
    }

    @Override
    BiFunction<LocalDate, LocalDate, Long> betweenFunction(ChronoUnit unit) {
        return unit::between;
    }

}
