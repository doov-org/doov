/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.DefaultOperator.rule;
import static io.doov.core.dsl.meta.MetadataType.RULE;

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
    public Operator getOperator() {
        return rule;
    }
    
    @Override
    public MetadataType type() {
        return RULE;
    }

    @Override
    public Stream<Metadata> children() {
        return Stream.of(value);
    }
}
