/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;

import java.util.ArrayDeque;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class NumericFunctionMetadata extends LeafPredicateMetadata<NumericFunctionMetadata>  {

    private NumericFunctionMetadata(Metadata metadata, MetadataType type) {
        super(new ArrayDeque<>(metadata.flatten()), type);
    }

    // times

    public static NumericFunctionMetadata timesMetadata(Metadata metadata, int multiplier) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(times).valueObject(multiplier);
    }

    // plus

    public static NumericFunctionMetadata plusMetadata(Metadata metadata, DslField<?> readable) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(plus).field(readable);
    }

    // lesser

    public static NumericFunctionMetadata lesserThanMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(lesser_than).valueObject(value);
    }

    public static NumericFunctionMetadata lesserThanMetadata(Metadata metadata, Readable field2) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(lesser_than).valueReadable(field2);
    }

    public static NumericFunctionMetadata lesserThanMetadata(Metadata metadata, DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(lesser_than).field(field2);
    }

    public static NumericFunctionMetadata lesserOrEqualsMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(lesser_or_equals).valueObject(value);
    }

    public static NumericFunctionMetadata lesserOrEqualsMetadata(Metadata metadata, DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(lesser_or_equals).field(field2);
    }

    // lesser

    public static NumericFunctionMetadata greaterThanMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(greater_than).valueObject(value);
    }

    public static NumericFunctionMetadata greaterThanMetadata(Metadata metadata, Readable field2) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(greater_than).valueReadable(field2);
    }

    public static NumericFunctionMetadata greaterThanMetadata(Metadata metadata, DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(greater_than).field(field2);
    }

    public static NumericFunctionMetadata greaterOrEqualsMetadata(Metadata metadata, Object value) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(greater_or_equals).valueObject(value);
    }

    public static NumericFunctionMetadata greaterOrEqualsMetadata(Metadata metadata, Readable field2) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(greater_or_equals)
                .valueReadable(field2);
    }

    public static NumericFunctionMetadata greaterOrEqualsMetadata(Metadata metadata,
            DslField<?> field2) {
        return new NumericFunctionMetadata(metadata, FIELD_PREDICATE).operator(greater_or_equals).field(field2);
    }
}
