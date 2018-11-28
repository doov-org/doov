/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;

import java.util.ArrayList;
import java.util.List;
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

    public Operator getOperator() {
        return operator;
    }

    @Override
    public Stream<Metadata> children() {
        return Stream.of(left, right);
    }

    @Override
    public MetadataType type() {
        return BINARY_PREDICATE;
    }
    

    @Override
    public List<Element> flatten() {
        final List<Element> flatten = new ArrayList<>(left.flatten());
        flatten.add(new Element(operator, OPERATOR));
        flatten.addAll(right.flatten());
        return flatten;
    }
}
