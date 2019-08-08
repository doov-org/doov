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
import static io.doov.core.dsl.meta.predicate.FieldMetadata.fieldMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.readableMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueMetadata;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;

public class NumericFunctionMetadata extends BinaryPredicateMetadata {

    public NumericFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    // when

    public static NumericFunctionMetadata whenMetadata(Metadata metadata, StepCondition condition) {
        return new NumericFunctionMetadata(metadata, when, condition.metadata());
    }

    // times

    public static NumericFunctionMetadata timesMetadata(Metadata metadata, int multiplier) {
        return new NumericFunctionMetadata(metadata, times, valueMetadata(multiplier));
    }

    // plus

    public static NumericFunctionMetadata plusMetadata(Metadata metadata, Metadata metadata2) {
        return new NumericFunctionMetadata(metadata, plus, metadata2);
    }

    public static NumericFunctionMetadata plusMetadata(Metadata metadata, DslField<?> readable) {
        return new NumericFunctionMetadata(metadata, plus, fieldMetadata(readable));
    }

    public static NumericFunctionMetadata plusMetadata(Metadata metadata, Readable function) {
        return new NumericFunctionMetadata(metadata, plus, readableMetadata(function));
    }

    public static NumericFunctionMetadata plusMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, plus, valueMetadata(value));
    }

    // minus

    public static NumericFunctionMetadata minusMetadata(Metadata metadata, Metadata metadata2) {
        return new NumericFunctionMetadata(metadata, minus, metadata2);
    }

    public static NumericFunctionMetadata minusMetadata(Metadata metadata, DslField<?> readable) {
        return new NumericFunctionMetadata(metadata, minus, fieldMetadata(readable));
    }

    public static NumericFunctionMetadata minusMetadata(Metadata metadata, Readable function) {
        return new NumericFunctionMetadata(metadata, minus, readableMetadata(function));
    }

    public static NumericFunctionMetadata minusMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, minus, valueMetadata(value));
    }

    // lesser

    public static NumericFunctionMetadata lesserThanMetadata(Metadata metadata, Metadata metadata2) {
        return new NumericFunctionMetadata(metadata, lesser_than, metadata2);
    }

    public static NumericFunctionMetadata lesserThanMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, lesser_than, valueMetadata(value));
    }

    public static NumericFunctionMetadata lesserThanMetadata(Metadata metadata, Readable field2) {
        return new NumericFunctionMetadata(metadata, lesser_than, readableMetadata(field2));
    }

    public static NumericFunctionMetadata lesserThanMetadata(Metadata metadata, DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, lesser_than, fieldMetadata(field2));
    }

    public static NumericFunctionMetadata lesserOrEqualsMetadata(Metadata metadata, Metadata metadata2) {
        return new NumericFunctionMetadata(metadata, lesser_or_equals, metadata2);
    }

    public static NumericFunctionMetadata lesserOrEqualsMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, lesser_or_equals, valueMetadata(value));
    }

    public static NumericFunctionMetadata lesserOrEqualsMetadata(Metadata metadata, Readable value) {
        return new NumericFunctionMetadata(metadata, lesser_or_equals, readableMetadata(value));
    }

    public static NumericFunctionMetadata lesserOrEqualsMetadata(Metadata metadata, DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, lesser_or_equals, fieldMetadata(field2));
    }

    // greater

    public static NumericFunctionMetadata greaterThanMetadata(Metadata metadata, Metadata metadata2) {
        return new NumericFunctionMetadata(metadata, greater_than, metadata2);
    }

    public static NumericFunctionMetadata greaterThanMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, greater_than, valueMetadata(value));
    }

    public static NumericFunctionMetadata greaterThanMetadata(Metadata metadata, Readable field2) {
        return new NumericFunctionMetadata(metadata, greater_than, readableMetadata(field2));
    }

    public static NumericFunctionMetadata greaterThanMetadata(Metadata metadata, DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, greater_than, fieldMetadata(field2));
    }

    public static NumericFunctionMetadata greaterOrEqualsMetadata(Metadata metadata, Metadata metadata2) {
        return new NumericFunctionMetadata(metadata, greater_or_equals, metadata2);
    }

    public static NumericFunctionMetadata greaterOrEqualsMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, greater_or_equals, valueMetadata(value));
    }

    public static NumericFunctionMetadata greaterOrEqualsMetadata(Metadata metadata, Readable field2) {
        return new NumericFunctionMetadata(metadata, greater_or_equals, readableMetadata(field2));
    }

    public static NumericFunctionMetadata greaterOrEqualsMetadata(Metadata metadata, DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, greater_or_equals, fieldMetadata(field2));
    }
}
