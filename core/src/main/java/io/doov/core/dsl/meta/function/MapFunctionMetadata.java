/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.ValuePredicateMetadata;

public class MapFunctionMetadata extends BinaryPredicateMetadata {

    public MapFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    public static MapFunctionMetadata mapToIntMetadata(Metadata metadata) {
        return new MapFunctionMetadata(metadata, as_a_number, new ValuePredicateMetadata(LEAF_PREDICATE).valueUnknown(""));
    }

    public static MapFunctionMetadata mapToStringMetadata(Metadata metadata) {
        return new MapFunctionMetadata(metadata, as_string, new ValuePredicateMetadata(LEAF_PREDICATE).valueUnknown(""));
    }

    public static MapFunctionMetadata mapAsMetadata(Metadata metadata, String readable) {
        return new MapFunctionMetadata(metadata, as, new ValuePredicateMetadata(LEAF_PREDICATE).valueReadable(() -> readable));
    }

    public static MapFunctionMetadata mapUsingMetadata(Metadata metadata, String readable, Readable condition) {
        return new MapFunctionMetadata(
                new MapFunctionMetadata(metadata, as, new ValuePredicateMetadata(LEAF_PREDICATE).valueReadable(() -> readable)),
                with, new ValuePredicateMetadata(LEAF_PREDICATE).valueReadable(condition));
    }
}
