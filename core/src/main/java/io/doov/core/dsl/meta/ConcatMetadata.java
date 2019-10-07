/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import java.util.List;
import java.util.stream.Stream;

public class ConcatMetadata extends AbstractMetadata {

    private List<Metadata> mappingInputs;

    public ConcatMetadata(List<Metadata> mappingInputs) {
        this.mappingInputs = mappingInputs;
    }

    public static Metadata create(List<Metadata> mappingInputs) {
        return new ConcatMetadata(mappingInputs);
    }

    @Override
    public Stream<Metadata> left() {
        return mappingInputs.stream();
    }

    @Override
    public MetadataType type() {
        return MetadataType.MAPPING_LEAF_ITERABLE_CONCAT;
    }
}
