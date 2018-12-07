/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.is;
import static io.doov.core.dsl.meta.DefaultOperator.not;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;

import java.util.ArrayDeque;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class BooleanFunctionMetadata extends LeafPredicateMetadata<BooleanFunctionMetadata> {

    private BooleanFunctionMetadata(Metadata metadata) {
        super(new ArrayDeque<>(metadata.flatten()), metadata.type());
    }

    private BooleanFunctionMetadata(Metadata metadata, MetadataType type) {
        super(new ArrayDeque<>(metadata.flatten()), type);
    }

    // boolean

    public static BooleanFunctionMetadata notMetadata(Metadata metadata) {
        return new BooleanFunctionMetadata(metadata, FIELD_PREDICATE).operator(not);
    }

    public static BooleanFunctionMetadata andMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, FIELD_PREDICATE).operator(and).valueObject(value);
    }

    public static BooleanFunctionMetadata andMetadata(Metadata metadata, Readable value) {
        return new BooleanFunctionMetadata(metadata, FIELD_PREDICATE).operator(and).valueReadable(value);
    }

    public static BooleanFunctionMetadata orMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, FIELD_PREDICATE).operator(or).valueObject(value);
    }

    public static BooleanFunctionMetadata orMetadata(Metadata metadata, Readable value) {
        return new BooleanFunctionMetadata(metadata, FIELD_PREDICATE).operator(or).valueReadable(value);
    }

    public static BooleanFunctionMetadata isMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, FIELD_PREDICATE).operator(is).valueObject(value);
    }
}
