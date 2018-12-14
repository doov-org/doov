/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;

import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.predicate.ValuePredicateMetadata;

public class TemporalAdjusterMetadata extends ValuePredicateMetadata<TemporalAdjusterMetadata> {

    public TemporalAdjusterMetadata(MetadataType type) {
        super(type);
    }

    public static TemporalAdjusterMetadata firstDayOfMonthMetadata() {
        return new TemporalAdjusterMetadata(LEAF_PREDICATE).operator(first_day_of_month);
    }

    public static TemporalAdjusterMetadata firstDayOfNextMonthMetadata() {
        return new TemporalAdjusterMetadata(LEAF_PREDICATE).operator(first_day_of_next_month);
    }

    public static TemporalAdjusterMetadata firstDayOfYearMetadata() {
        return new TemporalAdjusterMetadata(LEAF_PREDICATE).operator(first_day_of_year);
    }

    public static TemporalAdjusterMetadata firstDayOfNextYearMetadata() {
        return new TemporalAdjusterMetadata(LEAF_PREDICATE).operator(first_day_of_next_year);
    }

    public static TemporalAdjusterMetadata lastDayOfMonthMetadata() {
        return new TemporalAdjusterMetadata(LEAF_PREDICATE).operator(last_day_of_month);
    }

    public static TemporalAdjusterMetadata lastDayOfYearMetadata() {
        return new TemporalAdjusterMetadata(LEAF_PREDICATE).operator(last_day_of_year);
    }

    public static TemporalAdjusterMetadata unknownMetadata(String value) {
        return new TemporalAdjusterMetadata(LEAF_PREDICATE).valueUnknown(value);
    }

}
