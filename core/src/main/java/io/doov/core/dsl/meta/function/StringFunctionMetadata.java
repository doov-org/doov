/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class StringFunctionMetadata extends LeafPredicateMetadata<StringFunctionMetadata> {

    public StringFunctionMetadata(Metadata metadata) {
        super(metadata);
    }

    public StringFunctionMetadata(Metadata metadata, MetadataType type) {
        super(metadata, type);
    }

    public static StringFunctionMetadata matchesMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, FIELD_PREDICATE).operator(matches).valueString(value);
    }

    public static StringFunctionMetadata containsMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, FIELD_PREDICATE).operator(contains).valueString(value);
    }

    public static StringFunctionMetadata startsWithMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, FIELD_PREDICATE).operator(starts_with).valueString(value);
    }

    public static StringFunctionMetadata endsWithMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, FIELD_PREDICATE).operator(ends_with).valueString(value);
    }

    public static StringFunctionMetadata lengthIsMetadata(Metadata metadata) {
        return new StringFunctionMetadata(metadata, FIELD_PREDICATE).operator(length_is);
    }
}
