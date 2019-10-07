/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import java.util.stream.Stream;

public class MapEachMetadata extends AbstractMetadata {

    private final Metadata inputMetadata;
    private final Metadata converterMetadata;

    public MapEachMetadata(Metadata inputMetadata, Metadata converterMetadata) {
        this.inputMetadata = inputMetadata;
        this.converterMetadata = converterMetadata;
    }

    public static MapEachMetadata create(Metadata metadata, Metadata converterMetadata) {
        return new MapEachMetadata(metadata, converterMetadata);
    }

    @Override
    public MetadataType type() {
        return MetadataType.MAPPING_LEAF_FLAT_MAP;
    }

    @Override
    public Stream<Metadata> left() {
        return Stream.of(inputMetadata);
    }

    @Override
    public Stream<Metadata> right() {
        return Stream.of(converterMetadata);
    }

}
