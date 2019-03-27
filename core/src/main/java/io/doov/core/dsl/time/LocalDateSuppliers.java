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

import static io.doov.core.dsl.meta.function.TemporalFunctionMetadata.*;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

import io.doov.core.Try;
import io.doov.core.dsl.impl.time.LocalDateFunction;
import io.doov.core.dsl.impl.time.TemporalFunction;

/**
 * Common and useful local date suppliers. You can mock the clock by using {@link #setClock(Clock)}.
 */
public class LocalDateSuppliers {

    private static ThreadLocal<Clock> THREAD_CLOCK = new ThreadLocal<>();

    private LocalDateSuppliers() {
        // static
    }

    /**
     * Returns a clock from the given local date.
     *
     * @param localDate the date to set the clock at
     * @return the new clock
     */
    public static Clock createClockFrom(LocalDate localDate) {
        LocalDateTime time = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        return Clock.fixed(time.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

    /**
     * Returns true if the clock is set.
     *
     * @return true if set
     */
    public static boolean isClockSet() {
        return THREAD_CLOCK.get() != null;
    }

    /**
     * Sets the clock.
     *
     * @param clock the clock to set
     */
    public static void setClock(Clock clock) {
        THREAD_CLOCK.set(clock);
    }

    /**
     * Returns the clock.
     *
     * @return the clock
     */
    public static Clock getClock() {
        return isClockSet() ? THREAD_CLOCK.get() : Clock.systemDefaultZone();
    }

    /**
     * Removes the clock.
     */
    public static void setDefaultClock() {
        THREAD_CLOCK.remove();
    }

    /**
     * Return a temporal condition that returns today's local date.
     *
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> today() {
        return new LocalDateFunction(todayMetadata(),
                (model, context) -> Try.success(LocalDate.now(getClock())));
    }

    /**
     * Return a temporal condition that returns today's local date plus the given amount and unit.
     *
     * @param amountToAdd the amount to add
     * @param unit the unit of the amount
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> todayPlus(int amountToAdd, TemporalUnit unit) {
        return new LocalDateFunction(todayPlusMetadata(amountToAdd, unit),
                        (model, context) -> Try.success(LocalDate.now(getClock()).plus(amountToAdd, unit)));
    }

    /**
     * Return a temporal condition that returns today's local date plus the given amount in days.
     *
     * @param daysToAdd the amount of days to add
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> todayPlusDays(int daysToAdd) {
        return todayPlus(daysToAdd, ChronoUnit.DAYS);
    }

    /**
     * Return a temporal condition that returns today's local date plus the given amount in years.
     *
     * @param yearsToAdd the amount of years to add
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> todayPlusYears(int yearsToAdd) {
        return todayPlus(yearsToAdd, ChronoUnit.YEARS);
    }

    /**
     * Return a temporal condition that returns today's local date minus the given amount and unit.
     *
     * @param amountToSubstract the amount to remove
     * @param unit the unit of the amount
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> todayMinus(int amountToSubstract, TemporalUnit unit) {
        return new LocalDateFunction(todayMinusMetadata(amountToSubstract, unit),
                        (model, context) -> Try.success(LocalDate.now(getClock()).minus(amountToSubstract, unit)));
    }

    /**
     * Return a temporal condition that returns today's local date minus the given amount in days.
     *
     * @param daysToSubstract the amount of days to remove
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> todayMinusDays(int daysToSubstract) {
        return todayMinus(daysToSubstract, ChronoUnit.DAYS);
    }

    /**
     * Return a temporal condition that returns today's local date minus the given amount in years.
     *
     * @param yearsToSubstract the amount of years to remove
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> todayMinusYears(int yearsToSubstract) {
        return todayMinus(yearsToSubstract, ChronoUnit.YEARS);
    }

    /**
     * Return a temporal condition that returns today's local date at the first day of month.
     *
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> firstDayOfThisMonth() {
        return new LocalDateFunction(firstDayOfThisMonthMetadata(),
                        (model, context) -> Try.success(LocalDate.now(getClock()).with(firstDayOfMonth())));
    }

    /**
     * Return a temporal condition that returns today's local date at the first day of year.
     *
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> firstDayOfThisYear() {
        return new LocalDateFunction(firstDayOfThisYearMetadata(),
                        (model, context) -> Try.success(LocalDate.now(getClock()).with(firstDayOfYear())));
    }

    /**
     * Return a temporal condition that returns today's local date at the last day of month.
     *
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> lastDayOfThisMonth() {
        return new LocalDateFunction(lastDayOfThisMonthMetadata(),
                        (model, context) -> Try.success(LocalDate.now(getClock()).with(lastDayOfMonth())));
    }

    /**
     * Return a temporal condition that returns today's local date at the last day of year.
     *
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> lastDayOfThisYear() {
        return new LocalDateFunction(lastDayOfThisYearMetadata(),
                        (model, context) -> Try.success(LocalDate.now(getClock()).with(lastDayOfYear())));
    }

    /**
     * Return a temporal condition that returns the local date from the given year, month, and day.
     *
     * @param year the year of the date
     * @param month the month of the date
     * @param dayOfMonth the day of the date
     * @return the temporal condition
     */
    public static TemporalFunction<LocalDate> date(int year, int month, int dayOfMonth) {
        return new LocalDateFunction(dateMetadata(LocalDate.of(year, month, dayOfMonth)),
                        (model, context) -> Try.success(LocalDate.of(year, month, dayOfMonth)));
    }

}
