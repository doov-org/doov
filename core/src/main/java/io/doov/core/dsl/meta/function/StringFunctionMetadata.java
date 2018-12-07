/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;

import java.util.ArrayDeque;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class StringFunctionMetadata extends LeafPredicateMetadata<StringFunctionMetadata>  {

    private StringFunctionMetadata(Metadata metadata) {
        super(new ArrayDeque<>(metadata.flatten()), metadata.type());
    }

    private StringFunctionMetadata(Metadata metadata, MetadataType type) {
        super(new ArrayDeque<>(metadata.flatten()), type);
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
