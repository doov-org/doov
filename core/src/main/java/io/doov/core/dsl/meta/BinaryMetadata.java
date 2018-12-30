/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;

import java.util.stream.Stream;

public class BinaryMetadata extends AbstractMetadata {
    private final Metadata left;
    private final Operator operator;
    private final Metadata right;

    public BinaryMetadata(Metadata left, Operator operator, Metadata right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Metadata getLeft() {
        return left;
    }

    public Metadata getRight() {
        return right;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public Stream<Metadata> left() {
        return Stream.of(left);
    }

    @Override
    public Stream<Metadata> right() {
        return Stream.of(right);
    }

    @Override
    public MetadataType type() {
        return BINARY_PREDICATE;
    }

}
