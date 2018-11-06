/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.as;
import static io.doov.core.dsl.meta.DefaultOperator.as_a_number;
import static io.doov.core.dsl.meta.DefaultOperator.as_string;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class MapFunctionMetadata extends LeafPredicateMetadata<MapFunctionMetadata> {
    public MapFunctionMetadata(Metadata metadata) {
        super(metadata);
    }

    public MapFunctionMetadata(Metadata metadata, MetadataType type) {
        super(metadata, type);
    }

    public static MapFunctionMetadata mapToIntMetadata(Metadata metadata) {
        return new MapFunctionMetadata(metadata, FIELD_PREDICATE).operator(as_a_number).valueUnknown("");
    }

    public static MapFunctionMetadata mapToStringMetadata(Metadata metadata) {
        return new MapFunctionMetadata(metadata, FIELD_PREDICATE).operator(as_string).valueUnknown("");
    }

    public static MapFunctionMetadata mapAsMetadata(Metadata metadata, String readable) {
        return new MapFunctionMetadata(metadata, FIELD_PREDICATE).operator(as).valueReadable(() -> readable);
    }

    public static MapFunctionMetadata mapUsingMetadata(Metadata metadata, String readable,
            Readable condition) {
        return new MapFunctionMetadata(metadata, FIELD_PREDICATE).operator(as).valueReadable(() -> readable)
                .operator(DefaultOperator.with).valueReadable(condition);
    }
}
