/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.DefaultOperator.rule;
import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.MetadataType.RULE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RuleMetadata extends AbstractMetadata {

    private final Metadata value;

    public RuleMetadata(Metadata value) {
        this.value = value;
    }

    public static RuleMetadata rule(Metadata value) {
        return new RuleMetadata(value);
    }

    @Override
    public MetadataType type() {
        return RULE;
    }

    @Override
    public List<Element> flatten() {
        final List<Element> flatten = new ArrayList<>();
        flatten.add(new Element(rule, OPERATOR));
        flatten.addAll(value.flatten());
        return flatten;
    }

    @Override
    public Stream<Metadata> children() {
        return Stream.of(value);
    }
}
