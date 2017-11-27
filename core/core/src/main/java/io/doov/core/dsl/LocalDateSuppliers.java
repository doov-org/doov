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
package io.doov.core.dsl;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

import io.doov.core.dsl.impl.LocalDateCondition;
import io.doov.core.dsl.impl.TemporalCondition;
import io.doov.core.dsl.meta.TextMetadata;

public class LocalDateSuppliers {

    private LocalDateSuppliers() {
        // static
    }

    // today

    public static TemporalCondition<LocalDate> today() {
        return new LocalDateCondition(null, new TextMetadata("today"),
                        (model, context) -> Optional.of(LocalDate.now()));
    }

    public static TemporalCondition<LocalDate> todayPlus(int amountToAdd, TemporalUnit unit) {
        return new LocalDateCondition(null, new TextMetadata("today plus " + amountToAdd + " " + unit),
                        (model, context) -> Optional.of(LocalDate.now().plus(amountToAdd, unit)));
    }

    public static TemporalCondition<LocalDate> todayPlusDays(int daysToAdd) {
        return todayPlus(daysToAdd, ChronoUnit.DAYS);
    }

    public static TemporalCondition<LocalDate> todayPlusYears(int yearsToAdd) {
        return todayPlus(yearsToAdd, ChronoUnit.YEARS);
    }

    public static TemporalCondition<LocalDate> todayMinus(int amountToSubstract, TemporalUnit unit) {
        return new LocalDateCondition(null, new TextMetadata("today minus " + amountToSubstract + " " + unit),
                        (model, context) -> Optional.of(LocalDate.now().minus(amountToSubstract, unit)));
    }

    public static TemporalCondition<LocalDate> todayMinusDays(int daysToSubstract) {
        return todayMinus(daysToSubstract, ChronoUnit.DAYS);
    }

    public static TemporalCondition<LocalDate> todayMinusYears(int yearsToSubstract) {
        return todayMinus(yearsToSubstract, ChronoUnit.YEARS);
    }

    // date

    public static TemporalCondition<LocalDate> date(int year, int month, int dayOfMonth) {
        return new LocalDateCondition(null, new TextMetadata(LocalDate.of(year, month, dayOfMonth).toString()),
                        (model, context) -> Optional.of(LocalDate.of(year, month, dayOfMonth)));
    }

    // adjusters

    public static TemporalCondition<LocalDate> lastDayOfThisMonth() {
        return new LocalDateCondition(null, new TextMetadata("first day of this month"),
                        (model, context) -> Optional.of(LocalDate.now().with(lastDayOfMonth())));
    }

    public static TemporalCondition<LocalDate> firstDayOfThisYear() {
        return new LocalDateCondition(null, new TextMetadata("first day of this year"),
                        (model, context) -> Optional.of(LocalDate.now().with(firstDayOfYear())));
    }

}
