/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueListMetadata;

import java.util.Collection;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;

public class IterableFunctionMetadata extends BinaryPredicateMetadata {

    public IterableFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    public static IterableFunctionMetadata containsMetadata(Metadata metadata, Object value) {
        return new IterableFunctionMetadata(metadata, contains, valueMetadata(value));
    }

    public static IterableFunctionMetadata containsMetadata(Metadata metadata, Collection<Object> values) {
        return new IterableFunctionMetadata(metadata, contains, valueListMetadata(values));
    }

    // size

    public static IterableFunctionMetadata hasSizeMetadata(Metadata metadata, int size) {
        return new IterableFunctionMetadata(metadata, has_size, valueMetadata(size));
    }

    public static IterableFunctionMetadata hasNotSizeMetadata(Metadata metadata, int size) {
        return new IterableFunctionMetadata(metadata, has_not_size, valueMetadata(size));
    }

}
