/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ElementType.TEMPORAL_UNIT;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;

import java.util.ArrayDeque;
import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class TemporalBiFunctionMetadata extends LeafPredicateMetadata<TemporalBiFunctionMetadata> {

    private TemporalBiFunctionMetadata(Metadata metadata, MetadataType type) {
        super(new ArrayDeque<>(metadata.flatten()), type);
    }
    
    // minus

    public static TemporalBiFunctionMetadata minusMetadata(Metadata metadata, DslField<?> field2, Object unit) {
        return new TemporalBiFunctionMetadata(metadata, FIELD_PREDICATE).operator(minus).field(field2).temporalUnit(unit);
    }

    public static TemporalBiFunctionMetadata minusMetadata(Metadata metadata, int value, Object unit) {
        return new TemporalBiFunctionMetadata(metadata, FIELD_PREDICATE).operator(minus).valueObject(value)
                .temporalUnit(unit);
    }

    // plus

    public static TemporalBiFunctionMetadata plusMetadata(Metadata metadata, int value, Object unit) {
        return new TemporalBiFunctionMetadata(metadata, FIELD_PREDICATE).operator(plus).valueObject(value)
                .temporalUnit(unit);
    }

    public static TemporalBiFunctionMetadata plusMetadata(Metadata metadata, DslField<?> field2, Object unit) {
        return new TemporalBiFunctionMetadata(metadata, FIELD_PREDICATE).operator(plus).field(field2).temporalUnit(unit);
    }

    // age at

    public static TemporalBiFunctionMetadata ageAtValueMetadata(DefaultCondition<?> condition, Object value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(age_at)
                .valueObject(value);
    }

    public static TemporalBiFunctionMetadata ageAtTemporalFieldMetadata(DefaultCondition<?> condition,
                                                                      DslField<?> field) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(age_at).field(field);
    }

    public static TemporalBiFunctionMetadata ageAtTemporalConditionMetadata(DefaultCondition<?> c1,
                                                                          DefaultCondition<?> c2) {
        return new TemporalBiFunctionMetadata(c1.getMetadata(), FIELD_PREDICATE).operator(age_at).valueCondition(c2);
    }

    public static TemporalBiFunctionMetadata ageAtSupplierMetadata(DefaultCondition<?> condition, Supplier<?> supplier) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(age_at)
                .valueSupplier(supplier);
    }

    // after

    public static TemporalBiFunctionMetadata afterValueMetadata(DefaultCondition<?> condition,
                                                              Object value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(after)
                .valueObject(value);
    }

    public static TemporalBiFunctionMetadata afterTemporalFieldMetadata(DefaultCondition<?> condition,
                                                                      DslField<?> field) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(after).field(field);
    }

    public static TemporalBiFunctionMetadata afterTemporalConditionMetadata(DefaultCondition<?> c1,
                                                                          DefaultCondition<?> c2) {
        return new TemporalBiFunctionMetadata(c1.getMetadata(), FIELD_PREDICATE).operator(after).valueCondition(c2);
    }

    public static TemporalBiFunctionMetadata afterSupplierMetadata(DefaultCondition<?> condition,
                                                                 Supplier<?> value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(after)
                .valueSupplier(value);
    }

    public static TemporalBiFunctionMetadata afterOrEqualsValueMetadata(DefaultCondition<?> condition,
                                                                      Object value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(after_or_equals)
                .valueObject(value);
    }

    public static TemporalBiFunctionMetadata afterOrEqTemporalFieldMetadata(DefaultCondition<?> condition,
                                                                          DslField<?> field) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(after_or_equals)
                .field(field);
    }

    public static TemporalBiFunctionMetadata afterOrEqualsSupplierMetadata(DefaultCondition<?> condition,
                                                                         Supplier<?> value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(after_or_equals)
                .valueSupplier(value);
    }

    public static TemporalBiFunctionMetadata afterOrEqualsTemporalConditionMetadata(DefaultCondition<?> c1,
                                                                                  DefaultCondition<?> c2) {
        return new TemporalBiFunctionMetadata(c1.getMetadata(), FIELD_PREDICATE).operator(after_or_equals)
                .valueCondition(c2);
    }

    // before

    public static TemporalBiFunctionMetadata beforeValueMetadata(DefaultCondition<?> condition,
                                                               Object value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(before)
                .valueObject(value);
    }

    public static TemporalBiFunctionMetadata beforeTemporalFieldMetadata(DefaultCondition<?> condition,
                                                                       DslField<?> field) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(before).field(field);
    }

    public static TemporalBiFunctionMetadata beforeTemporalConditionMetadata(DefaultCondition<?> c1,
                                                                           DefaultCondition<?> c2) {
        return new TemporalBiFunctionMetadata(c1.getMetadata(), FIELD_PREDICATE).operator(before).valueCondition(c2);
    }

    public static TemporalBiFunctionMetadata beforeSupplierMetadata(DefaultCondition<?> condition,
                                                                  Supplier<?> value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(before)
                .valueSupplier(value);
    }

    public static TemporalBiFunctionMetadata beforeOrEqualsValueMetadata(DefaultCondition<?> condition,
                                                                       Object value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(before_or_equals)
                .valueObject(value);
    }

    public static TemporalBiFunctionMetadata beforeOrEqTemporalFieldMetadata(DefaultCondition<?> condition,
                                                                           DslField<?> field) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(before_or_equals)
                .field(field);
    }

    public static TemporalBiFunctionMetadata beforeOrEqualsSupplierMetadata(DefaultCondition<?> condition,
                                                                          Supplier<?> value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), FIELD_PREDICATE).operator(before_or_equals)
                .valueSupplier(value);
    }

    public static TemporalBiFunctionMetadata beforeOrEqualsTemporalConditionMetadata(DefaultCondition<?> c1,
                                                                                   DefaultCondition<?> c2) {
        return new TemporalBiFunctionMetadata(c1.getMetadata(), FIELD_PREDICATE).operator(before_or_equals).valueCondition(c2);
    }

    private TemporalBiFunctionMetadata temporalUnit(Object unit) {
        return add(unit == null ? null : new Element(() -> unit.toString().toLowerCase(), TEMPORAL_UNIT));
    }
}
