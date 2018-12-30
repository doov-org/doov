/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.MetadataType.UNARY_PREDICATE;

import java.util.stream.Stream;

public class UnaryMetadata extends AbstractMetadata {
    private final Operator operator;
    private final Metadata value;

    public UnaryMetadata(Operator operator, Metadata value) {
        this.operator = operator;
        this.value = value;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    public Metadata getValue() {
        return value;
    }

    @Override
    public MetadataType type() {
        return UNARY_PREDICATE;
    }

    @Override
    public Stream<Metadata> right() {
        return Stream.of(value);
    }
}
