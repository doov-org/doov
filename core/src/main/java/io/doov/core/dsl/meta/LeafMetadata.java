/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static java.util.stream.Collectors.toList;

import java.util.*;

public class LeafMetadata extends AbstractMetadata {
    private final MetadataType type;
    private final Deque<Element> elements;

    public LeafMetadata(Deque<Element> elements, MetadataType type) {
        this.elements = elements;
        this.type = type;
    }

    @Override
    public MetadataType type() {
        return type;
    }
    
    public Deque<Element> elements() {
        return elements;
    }

    public List<Element> elementsAsList() {
        return elements.stream().collect(toList());
    }

    @Override
    public List<Element> flatten() {
        return new ArrayList<>(elements);
    }
}
