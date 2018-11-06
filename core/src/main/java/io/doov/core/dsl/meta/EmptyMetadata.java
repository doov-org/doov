/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import java.util.Locale;

public class EmptyMetadata extends AbstractMetadata {

    @Override
    public String readable(Locale locale) {
        return "";
    }

    @Override
    public MetadataType type() {
        return MetadataType.EMPTY;
    }
}
