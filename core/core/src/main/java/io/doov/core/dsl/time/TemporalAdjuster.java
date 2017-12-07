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

import static io.doov.core.dsl.meta.FieldMetadata.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.function.UnaryOperator;

import io.doov.core.dsl.meta.Metadata;

public class TemporalAdjuster {

    private final Metadata metadata;
    private final java.time.temporal.TemporalAdjuster adjuster;

    private TemporalAdjuster(Metadata metadata, java.time.temporal.TemporalAdjuster adjuster) {
        this.metadata = metadata;
        this.adjuster = adjuster;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public java.time.temporal.TemporalAdjuster getAdjuster() {
        return adjuster;
    }

    // adjusters

    public static TemporalAdjuster firstDayOfMonth() {
        return new TemporalAdjuster(firstDayOfMonthMetadata(), TemporalAdjusters.firstDayOfMonth());
    }

    public static TemporalAdjuster firstDayOfNextMonth() {
        return new TemporalAdjuster(firstDayOfNextMonthMetadata(), TemporalAdjusters.firstDayOfNextMonth());
    }

    public static TemporalAdjuster firstDayOfYear() {
        return new TemporalAdjuster(firstDayOfYearMetadata(), TemporalAdjusters.firstDayOfYear());
    }

    public static TemporalAdjuster firstDayOfNextYear() {
        return new TemporalAdjuster(firstDayOfNextYearMetadata(), TemporalAdjusters.firstDayOfNextYear());
    }

    public static TemporalAdjuster lastDayOfMonth() {
        return new TemporalAdjuster(lastDayOfMonthMetadata(), TemporalAdjusters.lastDayOfMonth());
    }

    public static TemporalAdjuster lastDayOfYear() {
        return new TemporalAdjuster(lastDayOfYearMetadata(), TemporalAdjusters.lastDayOfYear());
    }

    // date

    public static TemporalAdjuster ofDateAdjuster(UnaryOperator<LocalDate> dateBasedAdjuster) {
        return new TemporalAdjuster(unknownMetadata("of date adjuster"),
                TemporalAdjusters.ofDateAdjuster(dateBasedAdjuster));
    }

}
