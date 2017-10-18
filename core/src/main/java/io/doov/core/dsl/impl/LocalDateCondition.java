/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.time.LocalDate;
import java.util.function.Function;

import io.doov.core.dsl.field.LocalDateFieldInfo;

public class LocalDateCondition extends TemporalCondition<LocalDateFieldInfo, LocalDate> {

    public LocalDateCondition(LocalDateFieldInfo field) {
        super(field);
    }

    @Override
    Function<LocalDate, Boolean> afterFunction(LocalDate value) {
        return v -> v.isAfter(value);
    }

    @Override
    Function<LocalDate, Boolean> beforeFunction(LocalDate value) {
        return v -> v.isBefore(value);
    }

}
