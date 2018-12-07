/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;

import java.util.ArrayDeque;
import java.util.Collection;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class IterableFunctionMetadata extends LeafPredicateMetadata<IterableFunctionMetadata> {

    private IterableFunctionMetadata(Metadata metadata) {
        super(new ArrayDeque<>(metadata.flatten()), metadata.type());
    }

    private IterableFunctionMetadata(Metadata metadata, MetadataType type) {
        super(new ArrayDeque<>(metadata.flatten()), type);
    }

    public static IterableFunctionMetadata containsMetadata(Metadata metadata, Object value) {
        return new IterableFunctionMetadata(metadata, FIELD_PREDICATE).operator(contains).valueObject(value);
    }

    public static IterableFunctionMetadata containsMetadata(Metadata metadata, Collection<Object> values) {
        return new IterableFunctionMetadata(metadata, FIELD_PREDICATE).operator(contains).valueListObject(values);
    }

    // empty

    public static IterableFunctionMetadata isEmptyMetadata(Metadata metadata) {
        return new IterableFunctionMetadata(metadata, FIELD_PREDICATE).operator(is_empty);
    }

    public static IterableFunctionMetadata isNotEmptyMetadata(Metadata metadata) {
        return new IterableFunctionMetadata(metadata, FIELD_PREDICATE).operator(is_not_empty);
    }

    // size

    public static IterableFunctionMetadata hasSizeMetadata(Metadata metadata, int size) {
        return new IterableFunctionMetadata(metadata, FIELD_PREDICATE).operator(has_size).valueObject(size);
    }

    public static IterableFunctionMetadata hasNotSizeMetadata(Metadata metadata, int size) {
        return new IterableFunctionMetadata(metadata, FIELD_PREDICATE).operator(has_not_size).valueObject(size);
    }

}
