/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.stringMetadata;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;

public class StringFunctionMetadata extends BinaryPredicateMetadata {

    public StringFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    public static StringFunctionMetadata matchesMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, matches, stringMetadata(value));
    }

    public static StringFunctionMetadata containsMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, contains, stringMetadata(value));
    }

    public static StringFunctionMetadata startsWithMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, starts_with, stringMetadata(value));
    }

    public static StringFunctionMetadata endsWithMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, ends_with, stringMetadata(value));
    }

}
