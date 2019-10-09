/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.MappingOperator.map_each;

public class MapEachMetadata extends BinaryMetadata {

    public MapEachMetadata(Metadata inputMetadata, Metadata converterMetadata) {
        super(inputMetadata, map_each, converterMetadata);
    }

    public static MapEachMetadata create(Metadata metadata, Metadata converterMetadata) {
        return new MapEachMetadata(metadata, converterMetadata);
    }

    @Override
    public MetadataType type() {
        return MetadataType.MAPPING_LEAF_FLAT_MAP;
    }

}
