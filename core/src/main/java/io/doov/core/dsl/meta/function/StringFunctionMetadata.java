/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.ValuePredicateMetadata;

public class StringFunctionMetadata extends BinaryPredicateMetadata {

    public StringFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    public static StringFunctionMetadata matchesMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, matches,
                new ValuePredicateMetadata<>(LEAF_PREDICATE).valueString(value));
    }

    public static StringFunctionMetadata containsMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, contains,
                new ValuePredicateMetadata<>(LEAF_PREDICATE).valueString(value));
    }

    public static StringFunctionMetadata startsWithMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, starts_with,
                new ValuePredicateMetadata<>(LEAF_PREDICATE).valueString(value));
    }

    public static StringFunctionMetadata endsWithMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, ends_with,
                new ValuePredicateMetadata<>(LEAF_PREDICATE).valueString(value));
    }

}
