/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class BooleanFunctionMetadata extends LeafPredicateMetadata<BooleanFunctionMetadata> {
    
    public BooleanFunctionMetadata(Metadata metadata) {
        super(metadata);
    }

    public BooleanFunctionMetadata(Metadata metadata, MetadataType type) {
        super(metadata, type);
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

    public static BooleanFunctionMetadata xorMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, FIELD_PREDICATE).operator(xor).valueObject(value);
    }

    public static BooleanFunctionMetadata xorMetadata(Metadata metadata, Readable value) {
        return new BooleanFunctionMetadata(metadata, FIELD_PREDICATE).operator(xor).valueReadable(value);
    }


    public static BooleanFunctionMetadata isMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, FIELD_PREDICATE).operator(is).valueObject(value);
    }
}
