/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.DefaultOperator.when;
import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.MetadataType.WHEN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WhenMetadata extends AbstractMetadata {
    private final Metadata value;

    private WhenMetadata(Metadata value) {
        this.value = value;
    }

    public static WhenMetadata when(Metadata value) {
        return new WhenMetadata(value);
    }

    @Override
    public MetadataType type() {
        return WHEN;
    }

    @Override
    public List<Element> flatten() {
        final List<Element> flatten = new ArrayList<>();
        flatten.add(new Element(when, OPERATOR));
        flatten.addAll(value.flatten());
        return flatten;
    }

    @Override
    public Stream<Metadata> children() {
        return Stream.of(value);
    }
}
