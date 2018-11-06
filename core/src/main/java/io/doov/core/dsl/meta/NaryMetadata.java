/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    public List<Element> flatten() {
        final List<Element> flatten = new ArrayList<>();
        flatten.add(new Element(operator, OPERATOR));
        flatten.addAll(values.stream().map(Metadata::flatten).flatMap(List::stream).collect(Collectors.toList()));
        return flatten;
    }

    @Override
    public Stream<Metadata> right() {
        return values.stream();
    }
}
