/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.time.LocalTime;
import java.util.function.Function;

import io.doov.core.dsl.field.LocalTimeFieldInfo;

public class LocalTimeCondition extends TemporalCondition<LocalTimeFieldInfo, LocalTime> {

    public LocalTimeCondition(LocalTimeFieldInfo field) {
        super(field);
    }

    @Override
    Function<LocalTime, Boolean> afterFunction(LocalTime value) {
        return v -> v.isAfter(value);
    }

    @Override
    Function<LocalTime, Boolean> beforeFunction(LocalTime value) {
        return v -> v.isBefore(value);
    }

}
