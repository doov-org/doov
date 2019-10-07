/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import java.util.stream.Stream;

public class ReduceMetadata extends AbstractMetadata {

    private Metadata mappingInputMetadata;
    private Metadata converterMetadata;

    public ReduceMetadata(Metadata mappingInputMetadata, Metadata converterMetadata) {
        this.mappingInputMetadata = mappingInputMetadata;
        this.converterMetadata = converterMetadata;
    }

    public static ReduceMetadata create(Metadata mappingInput, Metadata converter) {
        return new ReduceMetadata(mappingInput, converter);
    }

    @Override
    public Stream<Metadata> left() {
        return Stream.of(mappingInputMetadata);
    }

    @Override
    public Stream<Metadata> right() {
        return Stream.of(converterMetadata);
    }

    @Override
    public MetadataType type() {
        return MetadataType.MAPPING_LEAF_REDUCE;
    }
}
