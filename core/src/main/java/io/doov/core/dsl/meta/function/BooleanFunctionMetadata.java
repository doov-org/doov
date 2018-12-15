/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.readableMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueMetadata;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.ValuePredicateMetadata;

public class BooleanFunctionMetadata extends BinaryPredicateMetadata {

    public BooleanFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    // boolean

    public static BooleanFunctionMetadata andMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, and, valueMetadata(value));
    }

    public static BooleanFunctionMetadata andMetadata(Metadata metadata, Readable value) {
        return new BooleanFunctionMetadata(metadata, and, readableMetadata(value));
    }

    public static BooleanFunctionMetadata orMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, or, valueMetadata(value));
    }

    public static BooleanFunctionMetadata orMetadata(Metadata metadata, Readable value) {
        return new BooleanFunctionMetadata(metadata, or, readableMetadata(value));
    }

    public static BooleanFunctionMetadata isMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, is, valueMetadata(value));
    }
}
