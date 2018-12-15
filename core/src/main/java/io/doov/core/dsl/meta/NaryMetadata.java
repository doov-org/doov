/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;

import java.util.List;
import java.util.stream.Stream;

public class NaryMetadata extends AbstractMetadata {
    private final Operator operator;
    private final List<Metadata> values;

    public NaryMetadata(Operator operator, List<Metadata> values) {
        this.operator = operator;
        this.values = values;
    }

    public Operator getOperator() {
        return operator;
    }

    public List<Metadata> getValues() {
        return values;
    }

    @Override
    public MetadataType type() {
        return NARY_PREDICATE;
    }

    @Override
    public Stream<Metadata> right() {
        return values.stream();
    }
}
