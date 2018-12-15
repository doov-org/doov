/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.*;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.ValuePredicateMetadata;

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

    public static NumericFunctionMetadata plusMetadata(Metadata metadata, DslField<?> readable) {
        return new NumericFunctionMetadata(metadata, plus, fieldMetadata(readable));
    }

    // lesser

    public static NumericFunctionMetadata lesserThanMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, lesser_than, valueMetadata(value));
    }

    public static NumericFunctionMetadata lesserThanMetadata(Metadata metadata, Readable field2) {
        return new NumericFunctionMetadata(metadata, lesser_than, valueMetadata(field2));
    }

    public static NumericFunctionMetadata lesserThanMetadata(Metadata metadata, DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, lesser_than, fieldMetadata(field2));
    }

    public static NumericFunctionMetadata lesserOrEqualsMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, lesser_or_equals, valueMetadata(value));
    }

    public static NumericFunctionMetadata lesserOrEqualsMetadata(Metadata metadata, DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, lesser_or_equals, fieldMetadata(field2));
    }

    // lesser

    public static NumericFunctionMetadata greaterThanMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, greater_than, valueMetadata(value));
    }

    public static NumericFunctionMetadata greaterThanMetadata(Metadata metadata, Readable field2) {
        return new NumericFunctionMetadata(metadata, greater_than, readableMetadata(field2));
    }

    public static NumericFunctionMetadata greaterThanMetadata(Metadata metadata, DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, greater_than, fieldMetadata(field2));
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
