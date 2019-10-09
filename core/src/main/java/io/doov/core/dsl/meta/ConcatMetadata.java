/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.DefaultOperator.concat;

import java.util.List;

public class ConcatMetadata extends NaryMetadata {

    public ConcatMetadata(List<Metadata> mappingInputs) {
        super(concat, mappingInputs);
    }

    public static Metadata create(List<Metadata> mappingInputs) {
        return new ConcatMetadata(mappingInputs);
    }

    @Override
    public MetadataType type() {
        return MetadataType.MAPPING_LEAF_ITERABLE_CONCAT;
    }
}
