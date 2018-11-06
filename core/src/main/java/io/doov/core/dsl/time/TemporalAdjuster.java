/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.core.dsl.time;

import static io.doov.core.dsl.meta.function.TemporalAdjusterMetadata.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.function.UnaryOperator;

import io.doov.core.dsl.meta.function.TemporalAdjusterMetadata;

/**
 * Common and useful temporal adjusters. This class wraps {@link java.time.temporal.TemporalAdjuster} to add metadata
 * information to each adjusters.
 */
public class TemporalAdjuster {

    private final TemporalAdjusterMetadata metadata;
    private final java.time.temporal.TemporalAdjuster adjuster;

    public TemporalAdjuster(TemporalAdjusterMetadata metadata, java.time.temporal.TemporalAdjuster adjuster) {
        this.metadata = metadata;
        this.adjuster = adjuster;
    }

    public TemporalAdjusterMetadata getMetadata() {
        return metadata;
    }

    public java.time.temporal.TemporalAdjuster getAdjuster() {
        return adjuster;
    }

    /**
     * See {@link TemporalAdjusters#firstDayOfMonth()}
     *
     * @return the temporal adjuster
     * @see TemporalAdjusters#firstDayOfMonth()
     */
    public static TemporalAdjuster firstDayOfMonth() {
        return new TemporalAdjuster(firstDayOfMonthMetadata(), TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * See {@link TemporalAdjusters#firstDayOfNextMonth()}
     *
     * @return the temporal adjuster
     * @see TemporalAdjusters#firstDayOfNextMonth()
     */
    public static TemporalAdjuster firstDayOfNextMonth() {
        return new TemporalAdjuster(firstDayOfNextMonthMetadata(), TemporalAdjusters.firstDayOfNextMonth());
    }

    /**
     * See {@link TemporalAdjusters#firstDayOfYear()}
     *
     * @return the temporal adjuster
     * @see TemporalAdjusters#firstDayOfYear()
     */
    public static TemporalAdjuster firstDayOfYear() {
        return new TemporalAdjuster(firstDayOfYearMetadata(), TemporalAdjusters.firstDayOfYear());
    }

    /**
     * See {@link TemporalAdjusters#firstDayOfNextYear()}
     *
     * @return the temporal adjuster
     * @see TemporalAdjusters#firstDayOfNextYear()
     */
    public static TemporalAdjuster firstDayOfNextYear() {
        return new TemporalAdjuster(firstDayOfNextYearMetadata(), TemporalAdjusters.firstDayOfNextYear());
    }

    /**
     * See {@link TemporalAdjusters#lastDayOfMonth()}
     *
     * @return the temporal adjuster
     * @see TemporalAdjusters#lastDayOfMonth()
     */
    public static TemporalAdjuster lastDayOfMonth() {
        return new TemporalAdjuster(lastDayOfMonthMetadata(), TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * See {@link TemporalAdjusters#lastDayOfYear()}
     *
     * @return the temporal adjuster
     * @see TemporalAdjusters#lastDayOfYear()
     */
    public static TemporalAdjuster lastDayOfYear() {
        return new TemporalAdjuster(lastDayOfYearMetadata(), TemporalAdjusters.lastDayOfYear());
    }

    /**
     * See {@link TemporalAdjusters#ofDateAdjuster(UnaryOperator)}
     *
     * @param dateBasedAdjuster the adjuster to apply
     * @return the temporal adjuster
     * @see TemporalAdjusters#ofDateAdjuster(UnaryOperator)
     */
    public static TemporalAdjuster ofDateAdjuster(UnaryOperator<LocalDate> dateBasedAdjuster) {
        return ofDateAdjuster(unknownMetadata("of date adjuster"), dateBasedAdjuster);
    }

    /**
     * See {@link TemporalAdjusters#ofDateAdjuster(UnaryOperator)}
     *
     * @param metadata the metadata for the adjuster
     * @param dateBasedAdjuster the adjuster to apply
     * @return the temporal adjuster
     * @see TemporalAdjusters#ofDateAdjuster(UnaryOperator)
     */
    public static TemporalAdjuster ofDateAdjuster(TemporalAdjusterMetadata metadata,
                    UnaryOperator<LocalDate> dateBasedAdjuster) {
        return new TemporalAdjuster(metadata, TemporalAdjusters.ofDateAdjuster(dateBasedAdjuster));
    }

}
