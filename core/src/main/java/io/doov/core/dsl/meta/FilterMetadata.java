/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import java.util.stream.Stream;

public class FilterMetadata extends AbstractMetadata {

    private final Metadata mappingInputMetadata;
    private final Metadata converterMetadata;

    public FilterMetadata(Metadata mappingInputMetadata, Metadata converterMetadata) {
        this.mappingInputMetadata = mappingInputMetadata;
        this.converterMetadata = converterMetadata;
    }

    public static FilterMetadata create(Metadata mappingInput, Metadata converter) {
        return new FilterMetadata(mappingInput, converter);
    }

    @Override
    public MetadataType type() {
        return MetadataType.MAPPING_LEAF_FILTER;
    }

    @Override
    public Stream<Metadata> left() {
        return Stream.of(mappingInputMetadata);
    }

    @Override
    public Stream<Metadata> right() {
        return Stream.of(converterMetadata);
    }
}
