/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.time;

import java.time.LocalDate;
import java.time.temporal.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface LocalDateOperators extends TemporalOperators<LocalDate> {
    @Override
    default Function<LocalDate, LocalDate> minusFunction(int value, TemporalUnit unit) {
        return date -> date.minus(value, unit);
    }

    @Override
    default Function<LocalDate, LocalDate> plusFunction(int value, TemporalUnit unit) {
        return date -> date.plus(value, unit);
    }

    @Override
    default Function<LocalDate, LocalDate> withFunction(TemporalAdjuster ajuster) {
        return date -> date.with(ajuster);
    }

    @Override
    default BiFunction<LocalDate, LocalDate, Boolean> afterFunction() {
        return LocalDate::isAfter;
    }

    @Override
    default BiFunction<LocalDate, LocalDate, Boolean> afterOrEqualsFunction() {
        return (date1, date2) -> date1.isAfter(date2) || date1.equals(date2);
    }

    @Override
    default BiFunction<LocalDate, LocalDate, Boolean> beforeFunction() {
        return LocalDate::isBefore;
    }

    @Override
    default BiFunction<LocalDate, LocalDate, Boolean> beforeOrEqualsFunction() {
        return (date1, date2) -> date1.isBefore(date2) || date1.equals(date2);
    }

    @Override
    default BiFunction<LocalDate, LocalDate, Long> betweenFunction(ChronoUnit unit) {
        return unit::between;
    }

}
