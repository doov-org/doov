/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ElementType.TEMPORAL_UNIT;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;

import java.util.ArrayDeque;
import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class TemporalFunctionMetadata extends LeafPredicateMetadata<TemporalFunctionMetadata> {

    private TemporalFunctionMetadata(MetadataType type) {
        super(new ArrayDeque<>(), type);
    }

    private TemporalFunctionMetadata(Metadata metadata, MetadataType type) {
        super(new ArrayDeque<>(metadata.flatten()), type);
    }

    // minus

    public static TemporalFunctionMetadata minusMetadata(Metadata metadata, int value, Object unit) {
        return new TemporalFunctionMetadata(metadata, FIELD_PREDICATE).operator(minus).valueObject(value)
                .temporalUnit(unit);
    }

    public static TemporalFunctionMetadata minusMetadata(Metadata metadata, DslField<?> field2, Object unit) {
        return new TemporalFunctionMetadata(metadata, FIELD_PREDICATE).operator(minus).field(field2).temporalUnit(unit);
    }

    // plus

    public static TemporalFunctionMetadata plusMetadata(Metadata metadata, int value, Object unit) {
        return new TemporalFunctionMetadata(metadata, FIELD_PREDICATE).operator(plus).valueObject(value)
                .temporalUnit(unit);
    }

    public static TemporalFunctionMetadata plusMetadata(Metadata metadata, DslField<?> field2, Object unit) {
        return new TemporalFunctionMetadata(metadata, FIELD_PREDICATE).operator(plus).field(field2).temporalUnit(unit);
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

    // age at

    public static TemporalFunctionMetadata ageAtValueMetadata(DefaultCondition<?> condition, Object value) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(age_at)
                .valueObject(value);
    }

    public static TemporalFunctionMetadata ageAtTemporalFieldMetadata(DefaultCondition<?> condition,
            DslField<?> field) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(age_at).field(field);
    }

    public static TemporalFunctionMetadata ageAtTemporalConditionMetadata(DefaultCondition<?> c1,
            DefaultCondition<?> c2) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(c1).operator(age_at).valueCondition(c2);
    }

    public static TemporalFunctionMetadata ageAtSupplierMetadata(DefaultCondition<?> condition, Supplier<?> supplier) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(age_at)
                .valueSupplier(supplier);
    }

    // after

    public static TemporalFunctionMetadata afterValueMetadata(DefaultCondition<?> condition,
            Object value) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after)
                .valueObject(value);
    }

    public static TemporalFunctionMetadata afterTemporalFieldMetadata(DefaultCondition<?> condition,
            DslField<?> field) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after).field(field);
    }

    public static TemporalFunctionMetadata afterTemporalConditionMetadata(DefaultCondition<?> c1,
            DefaultCondition<?> c2) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(c1).operator(after).valueCondition(c2);
    }

    public static TemporalFunctionMetadata afterSupplierMetadata(DefaultCondition<?> condition,
            Supplier<?> value) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after)
                .valueSupplier(value);
    }

    public static TemporalFunctionMetadata afterOrEqualsValueMetadata(DefaultCondition<?> condition,
            Object value) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after_or_equals)
                .valueObject(value);
    }

    public static TemporalFunctionMetadata afterOrEqTemporalFieldMetadata(DefaultCondition<?> condition,
            DslField<?> field) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after_or_equals)
                .field(field);
    }

    public static TemporalFunctionMetadata afterOrEqualsSupplierMetadata(DefaultCondition<?> condition,
            Supplier<?> value) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after_or_equals)
                .valueSupplier(value);
    }

    public static TemporalFunctionMetadata afterOrEqualsTemporalConditionMetadata(DefaultCondition<?> c1,
            DefaultCondition<?> c2) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(c1).operator(after_or_equals)
                .valueCondition(c2);
    }

    // before

    public static TemporalFunctionMetadata beforeValueMetadata(DefaultCondition<?> condition,
            Object value) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before)
                .valueObject(value);
    }

    public static TemporalFunctionMetadata beforeTemporalFieldMetadata(DefaultCondition<?> condition,
            DslField<?> field) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before).field(field);
    }

    public static TemporalFunctionMetadata beforeTemporalConditionMetadata(DefaultCondition<?> c1,
            DefaultCondition<?> c2) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(c1).operator(before).valueCondition(c2);
    }

    public static TemporalFunctionMetadata beforeSupplierMetadata(DefaultCondition<?> condition,
            Supplier<?> value) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before)
                .valueSupplier(value);
    }

    public static TemporalFunctionMetadata beforeOrEqualsValueMetadata(DefaultCondition<?> condition,
            Object value) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before_or_equals)
                .valueObject(value);
    }

    public static TemporalFunctionMetadata beforeOrEqTemporalFieldMetadata(DefaultCondition<?> condition,
            DslField<?> field) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before_or_equals)
                .field(field);
    }

    public static TemporalFunctionMetadata beforeOrEqualsSupplierMetadata(DefaultCondition<?> condition,
            Supplier<?> value) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before_or_equals)
                .valueSupplier(value);
    }

    public static TemporalFunctionMetadata beforeOrEqualsTemporalConditionMetadata(DefaultCondition<?> c1,
            DefaultCondition<?> c2) {
        return new TemporalFunctionMetadata(FIELD_PREDICATE).valueCondition(c1).operator(before_or_equals)
                .valueCondition(c2);
    }
}
