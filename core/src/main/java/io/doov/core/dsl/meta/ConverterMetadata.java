/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.MetadataType.TYPE_CONVERTER;
import static io.doov.core.dsl.meta.MetadataType.TYPE_CONVERTER_IDENTITY;

import java.util.ArrayDeque;

public class ConverterMetadata extends LeafMetadata<ConverterMetadata> {

    public ConverterMetadata(MetadataType type) {
        super(type);
    }

    public static ConverterMetadata metadata(String description) {
        if (description == null || description.isEmpty()) {
            return new ConverterMetadata(TYPE_CONVERTER).valueUnknown(description);
        } else {
            return new ConverterMetadata(TYPE_CONVERTER).valueString(description);
        }
    }

    public static ConverterMetadata identity() {
        return new ConverterMetadata(TYPE_CONVERTER_IDENTITY).valueString("identity");
    }

}
