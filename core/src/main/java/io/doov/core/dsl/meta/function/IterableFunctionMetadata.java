/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;

import java.util.Collection;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.ValuePredicateMetadata;

public class IterableFunctionMetadata extends BinaryPredicateMetadata {

    public IterableFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    public static IterableFunctionMetadata containsMetadata(Metadata metadata, Object value) {
        return new IterableFunctionMetadata(metadata, contains,
                new ValuePredicateMetadata<>(LEAF_PREDICATE).valueObject(value));
    }

    public static IterableFunctionMetadata containsMetadata(Metadata metadata, Collection<Object> values) {
        return new IterableFunctionMetadata(metadata, contains,
                new ValuePredicateMetadata<>(LEAF_PREDICATE).valueListObject(values));
    }

    // size

    public static IterableFunctionMetadata hasSizeMetadata(Metadata metadata, int size) {
        return new IterableFunctionMetadata(metadata, has_size,
                new ValuePredicateMetadata<>(LEAF_PREDICATE).valueObject(size));
    }

    public static IterableFunctionMetadata hasNotSizeMetadata(Metadata metadata, int size) {
        return new IterableFunctionMetadata(metadata, has_not_size,
                new ValuePredicateMetadata<>(LEAF_PREDICATE).valueObject(size));
    }

}
