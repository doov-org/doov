/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl.time;

import static io.doov.core.dsl.meta.LeafMetadata.*;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

import io.doov.core.dsl.impl.LocalDateCondition;
import io.doov.core.dsl.impl.TemporalCondition;

public class LocalDateSuppliers {

    private static ThreadLocal<Clock> THREAD_CLOCK = new ThreadLocal<>();

    private LocalDateSuppliers() {
        // static
    }

    // Clock setters

    public static Clock createClockFrom(LocalDate localDate) {
        LocalDateTime time = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        return Clock.fixed(time.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

    public static boolean isClockSet() {
        return THREAD_CLOCK.get() != null;
    }

    public static void setClock(Clock clock) {
        THREAD_CLOCK.set(clock);
    }

    public static Clock getClock() {
        return isClockSet() ? THREAD_CLOCK.get() : Clock.systemDefaultZone();
    }

    public static void setDefaultClock() {
        THREAD_CLOCK.remove();
    }

    // today

    public static TemporalCondition<LocalDate> today() {
        return new LocalDateCondition(null, todayMetadata(),
                (model, context) -> Optional.of(LocalDate.now(getClock())));
    }

    public static TemporalCondition<LocalDate> todayPlus(int amountToAdd, TemporalUnit unit) {
        return new LocalDateCondition(null, todayPlusMetadata(amountToAdd, unit),
                (model, context) -> Optional.of(LocalDate.now(getClock()).plus(amountToAdd, unit)));
    }

    public static TemporalCondition<LocalDate> todayPlusDays(int daysToAdd) {
        return todayPlus(daysToAdd, ChronoUnit.DAYS);
    }

    public static TemporalCondition<LocalDate> todayPlusYears(int yearsToAdd) {
        return todayPlus(yearsToAdd, ChronoUnit.YEARS);
    }

    public static TemporalCondition<LocalDate> todayMinus(int amountToSubstract, TemporalUnit unit) {
        return new LocalDateCondition(null, todayMinusMetadata(amountToSubstract, unit),
                (model, context) -> Optional.of(LocalDate.now(getClock()).minus(amountToSubstract, unit)));
    }

    public static TemporalCondition<LocalDate> todayMinusDays(int daysToSubstract) {
        return todayMinus(daysToSubstract, ChronoUnit.DAYS);
    }

    public static TemporalCondition<LocalDate> todayMinusYears(int yearsToSubstract) {
        return todayMinus(yearsToSubstract, ChronoUnit.YEARS);
    }

    // adjusters

    public static TemporalCondition<LocalDate> firstDayOfThisMonth() {
        return new LocalDateCondition(null, firstDayOfThisMonthMetadata(),
                (model, context) -> Optional.of(LocalDate.now(getClock()).with(firstDayOfMonth())));
    }

    public static TemporalCondition<LocalDate> firstDayOfThisYear() {
        return new LocalDateCondition(null, firstDayOfThisYearMetadata(),
                (model, context) -> Optional.of(LocalDate.now(getClock()).with(firstDayOfYear())));
    }

    public static TemporalCondition<LocalDate> lastDayOfThisMonth() {
        return new LocalDateCondition(null, lastDayOfThisMonthMetadata(),
                (model, context) -> Optional.of(LocalDate.now(getClock()).with(lastDayOfMonth())));
    }

    public static TemporalCondition<LocalDate> lastDayOfThisYear() {
        return new LocalDateCondition(null, lastDayOfThisYearMetadata(),
                (model, context) -> Optional.of(LocalDate.now(getClock()).with(lastDayOfYear())));
    }

    // date

    public static TemporalCondition<LocalDate> date(int year, int month, int dayOfMonth) {
        return new LocalDateCondition(null, dateMetadata(LocalDate.of(year, month, dayOfMonth)),
                (model, context) -> Optional.of(LocalDate.of(year, month, dayOfMonth)));
    }

}
