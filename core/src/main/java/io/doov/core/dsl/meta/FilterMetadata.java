/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

public class FilterMetadata extends BinaryMetadata {

    public FilterMetadata(Metadata mappingInputMetadata, Metadata converterMetadata) {
        super(mappingInputMetadata, MappingOperator.filter, converterMetadata);
    }

    public static FilterMetadata create(Metadata mappingInput, Metadata converter) {
        return new FilterMetadata(mappingInput, converter);
    }

    @Override
    public MetadataType type() {
        return MetadataType.MAPPING_LEAF_FILTER;
    }

}
