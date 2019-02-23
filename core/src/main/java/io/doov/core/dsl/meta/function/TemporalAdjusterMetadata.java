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
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.LEAF_VALUE;

import java.util.concurrent.atomic.AtomicInteger;

import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class TemporalAdjusterMetadata extends LeafMetadata<TemporalAdjusterMetadata> implements PredicateMetadata {

    private final AtomicInteger evalTrue = new AtomicInteger();
    private final AtomicInteger evalFalse = new AtomicInteger();

    public TemporalAdjusterMetadata(MetadataType type) {
        super(type);
    }

    @Override
    public AtomicInteger evalTrue() {
        return evalTrue;
    }

    @Override
    public AtomicInteger evalFalse() {
        return evalFalse;
    }

    public static TemporalAdjusterMetadata firstDayOfMonthMetadata() {
        return new TemporalAdjusterMetadata(LEAF_VALUE).operator(first_day_of_month);
    }

    public static TemporalAdjusterMetadata firstDayOfNextMonthMetadata() {
        return new TemporalAdjusterMetadata(LEAF_VALUE).operator(first_day_of_next_month);
    }

    public static TemporalAdjusterMetadata firstDayOfYearMetadata() {
        return new TemporalAdjusterMetadata(LEAF_VALUE).operator(first_day_of_year);
    }

    public static TemporalAdjusterMetadata firstDayOfNextYearMetadata() {
        return new TemporalAdjusterMetadata(LEAF_VALUE).operator(first_day_of_next_year);
    }

    public static TemporalAdjusterMetadata lastDayOfMonthMetadata() {
        return new TemporalAdjusterMetadata(LEAF_VALUE).operator(last_day_of_month);
    }

    public static TemporalAdjusterMetadata lastDayOfYearMetadata() {
        return new TemporalAdjusterMetadata(LEAF_VALUE).operator(last_day_of_year);
    }

    public static TemporalAdjusterMetadata unknownMetadata(String value) {
        return new TemporalAdjusterMetadata(LEAF_VALUE).valueUnknown(value);
    }

}
