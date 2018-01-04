/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import java.util.Collections;
import java.util.List;

import io.doov.core.dsl.lang.Context;

public class EmptyMetadata implements Metadata {

    @Override
    public String readable() {
        return "";
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        visitor.visit(this, depth);
    }

    @Override
    public List<Metadata> children() {
        return Collections.emptyList();
    }

    @Override
    public MetadataType type() {
        return MetadataType.EMPTY;
    }

    @Override
    public Metadata message(Context context) {
        return this;
    }
}
