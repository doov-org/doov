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
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.LEAF_VALUE;
import static io.doov.core.dsl.meta.predicate.FieldMetadata.fieldMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueMetadata;

import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.ReduceType;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;

// TODO should be BinaryMetadata
public class TemporalBiFunctionMetadata extends BinaryPredicateMetadata {

    public TemporalBiFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    // temporal_minus

    public static TemporalBiFunctionMetadata minusMetadata(Metadata metadata, DslField<?> field2, Object unit) {
        return new TemporalBiFunctionMetadata(metadata, temporal_minus,
                new TemporalFunctionMetadata(FIELD_PREDICATE).field(field2).temporalUnit(unit));
    }

    public static TemporalBiFunctionMetadata minusMetadata(Metadata metadata, int value, Object unit) {
        return new TemporalBiFunctionMetadata(metadata, temporal_minus,
                new TemporalFunctionMetadata(LEAF_VALUE).valueObject(value).temporalUnit(unit));
    }

    // plus

    public static TemporalBiFunctionMetadata plusMetadata(Metadata metadata, int value, Object unit) {
        return new TemporalBiFunctionMetadata(metadata, plus,
                new TemporalFunctionMetadata(LEAF_VALUE).valueObject(value).temporalUnit(unit));
    }

    public static TemporalBiFunctionMetadata plusMetadata(Metadata metadata, DslField<?> field2, Object unit) {
        return new TemporalBiFunctionMetadata(metadata, plus,
                new TemporalFunctionMetadata(FIELD_PREDICATE).field(field2).temporalUnit(unit));
    }

    // age at

    public static TemporalBiFunctionMetadata ageAtValueMetadata(DefaultCondition<?> condition, Object value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), age_at, valueMetadata(value));
    }

    public static TemporalBiFunctionMetadata ageAtTemporalFieldMetadata(DefaultCondition<?> condition,
                                                                        DslField<?> field) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), age_at, fieldMetadata(field));
    }

    public static TemporalBiFunctionMetadata ageAtTemporalConditionMetadata(DefaultCondition<?> c1,
            DefaultCondition<?> c2) {
        return new TemporalBiFunctionMetadata(c1.getMetadata(), age_at, c2.getMetadata());
    }

    public static TemporalBiFunctionMetadata ageAtSupplierMetadata(DefaultCondition<?> condition, Supplier<?> supplier) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), age_at, valueMetadata(supplier));
    }

    // after

    public static TemporalBiFunctionMetadata afterValueMetadata(DefaultCondition<?> condition,
                                                                Object value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), after, valueMetadata(value));
    }

    public static TemporalBiFunctionMetadata afterTemporalFieldMetadata(DefaultCondition<?> condition,
                                                                        DslField<?> field) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), after, fieldMetadata(field));
    }

    public static TemporalBiFunctionMetadata afterTemporalConditionMetadata(DefaultCondition<?> c1,
            DefaultCondition<?> c2) {
        return new TemporalBiFunctionMetadata(c1.getMetadata(), after, c2.getMetadata());
    }

    public static TemporalBiFunctionMetadata afterSupplierMetadata(DefaultCondition<?> condition,
                                                                   Supplier<?> value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), after, valueMetadata(value));
    }

    public static TemporalBiFunctionMetadata afterOrEqualsValueMetadata(DefaultCondition<?> condition,
                                                                        Object value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), after_or_equals, valueMetadata(value));
    }

    public static TemporalBiFunctionMetadata afterOrEqTemporalFieldMetadata(DefaultCondition<?> condition,
                                                                            DslField<?> field) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), after_or_equals, fieldMetadata(field));
    }

    public static TemporalBiFunctionMetadata afterOrEqualsSupplierMetadata(DefaultCondition<?> condition,
                                                                           Supplier<?> value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), after_or_equals, valueMetadata(value));
    }

    public static TemporalBiFunctionMetadata afterOrEqualsTemporalConditionMetadata(DefaultCondition<?> c1,
            DefaultCondition<?> c2) {
        return new TemporalBiFunctionMetadata(c1.getMetadata(), after_or_equals, c2.getMetadata());
    }

    // before

    public static TemporalBiFunctionMetadata beforeValueMetadata(DefaultCondition<?> condition,
                                                                 Object value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), before, valueMetadata(value));
    }

    public static TemporalBiFunctionMetadata beforeTemporalFieldMetadata(DefaultCondition<?> condition,
                                                                         DslField<?> field) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), before, fieldMetadata(field));
    }

    public static TemporalBiFunctionMetadata beforeTemporalConditionMetadata(DefaultCondition<?> c1,
            DefaultCondition<?> c2) {
        return new TemporalBiFunctionMetadata(c1.getMetadata(), before, c2.getMetadata());
    }

    public static TemporalBiFunctionMetadata beforeSupplierMetadata(DefaultCondition<?> condition,
                                                                    Supplier<?> value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), before, valueMetadata(value));
    }

    public static TemporalBiFunctionMetadata beforeOrEqualsValueMetadata(DefaultCondition<?> condition,
                                                                         Object value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), before_or_equals, valueMetadata(value));
    }

    public static TemporalBiFunctionMetadata beforeOrEqTemporalFieldMetadata(DefaultCondition<?> condition,
                                                                             DslField<?> field) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), before_or_equals, fieldMetadata(field));
    }

    public static TemporalBiFunctionMetadata beforeOrEqualsSupplierMetadata(DefaultCondition<?> condition,
                                                                            Supplier<?> value) {
        return new TemporalBiFunctionMetadata(condition.getMetadata(), before_or_equals, valueMetadata(value));
    }

    public static TemporalBiFunctionMetadata beforeOrEqualsTemporalConditionMetadata(DefaultCondition<?> c1,
            DefaultCondition<?> c2) {
        return new TemporalBiFunctionMetadata(c1.getMetadata(), before_or_equals, c2.getMetadata());
    }


    // with
    public static TemporalBiFunctionMetadata withMetadata(Metadata metadata, TemporalAdjusterMetadata adjuster) {
        return new TemporalBiFunctionMetadata(metadata, with,
                new TemporalFunctionMetadata(LEAF_VALUE).add(adjuster.elements().getFirst()));
    }

    @Override
    public Metadata reduce(Context context, ReduceType type) {
        return this;
    }
}
