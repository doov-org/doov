/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.time;

import java.time.LocalDateTime;
import java.time.temporal.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface LocalDateTimeOperators extends TemporalOperators<LocalDateTime> {

    @Override
    default Function<LocalDateTime, LocalDateTime> minusFunction(int value, TemporalUnit unit) {
        return dateTime -> dateTime.minus(value, unit);
    }

    @Override
    default Function<LocalDateTime, LocalDateTime> plusFunction(int value, TemporalUnit unit) {
        return dateTime -> dateTime.plus(value, unit);
    }

    @Override
    default Function<LocalDateTime, LocalDateTime> withFunction(TemporalAdjuster ajuster) {
        return dateTime -> dateTime.with(ajuster);
    }

    @Override
    default BiFunction<LocalDateTime, LocalDateTime, Boolean> afterFunction() {
        return LocalDateTime::isAfter;
    }

    @Override
    default BiFunction<LocalDateTime, LocalDateTime, Boolean> afterOrEqualsFunction() {
        return (dateTime1, dateTime2) -> dateTime1.isAfter(dateTime2) || dateTime1.equals(dateTime2);
    }

    @Override
    default BiFunction<LocalDateTime, LocalDateTime, Boolean> beforeFunction() {
        return LocalDateTime::isBefore;
    }

    @Override
    default BiFunction<LocalDateTime, LocalDateTime, Boolean> beforeOrEqualsFunction() {
        return (dateTime1, dateTime2) -> dateTime1.isBefore(dateTime2) || dateTime1.equals(dateTime2);
    }

    @Override
    default BiFunction<LocalDateTime, LocalDateTime, Long> betweenFunction(ChronoUnit unit) {
        return unit::between;
    }
}
