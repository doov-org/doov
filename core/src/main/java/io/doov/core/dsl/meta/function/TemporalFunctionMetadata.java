/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ElementType.TEMPORAL_UNIT;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;

import java.util.ArrayDeque;

import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class TemporalFunctionMetadata extends LeafPredicateMetadata<TemporalFunctionMetadata> {

    private TemporalFunctionMetadata(MetadataType type) {
        super(new ArrayDeque<>(), type);
    }

    private TemporalFunctionMetadata temporalUnit(Object unit) {
        return add(unit == null ? null : new Element(() -> unit.toString().toLowerCase(), TEMPORAL_UNIT));
    }

    // local date suppliers

    public static TemporalFunctionMetadata todayMetadata() {
        return new TemporalFunctionMetadata(LEAF_PREDICATE).operator(today);
    }

    public static TemporalFunctionMetadata todayPlusMetadata(int value, Object unit) {
        return new TemporalFunctionMetadata(LEAF_PREDICATE).operator(today_plus).valueObject(value).temporalUnit(unit);
    }

    public static TemporalFunctionMetadata todayMinusMetadata(int value, Object unit) {
        return new TemporalFunctionMetadata(LEAF_PREDICATE).operator(today_minus).valueObject(value).temporalUnit(unit);
    }

    public static TemporalFunctionMetadata firstDayOfThisMonthMetadata() {
        return new TemporalFunctionMetadata(LEAF_PREDICATE).operator(first_day_of_this_month);
    }

    public static TemporalFunctionMetadata firstDayOfThisYearMetadata() {
        return new TemporalFunctionMetadata(LEAF_PREDICATE).operator(first_day_of_this_year);
    }

    public static TemporalFunctionMetadata lastDayOfThisMonthMetadata() {
        return new TemporalFunctionMetadata(LEAF_PREDICATE).operator(last_day_of_this_month);
    }

    public static TemporalFunctionMetadata lastDayOfThisYearMetadata() {
        return new TemporalFunctionMetadata(LEAF_PREDICATE).operator(last_day_of_this_year);
    }

    public static TemporalFunctionMetadata dateMetadata(Object date) {
        return new TemporalFunctionMetadata(LEAF_PREDICATE).valueString(date.toString());
    }

}
