/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.MappingOperator.reduce;

public class ReduceMetadata extends BinaryMetadata {

    public ReduceMetadata(Metadata mappingInputMetadata, Metadata converterMetadata) {
        super(mappingInputMetadata, reduce, converterMetadata);
    }

    public static ReduceMetadata create(Metadata mappingInput, Metadata converter) {
        return new ReduceMetadata(mappingInput, converter);
    }

    @Override
    public MetadataType type() {
        return MetadataType.MAPPING_LEAF_REDUCE;
    }
}
