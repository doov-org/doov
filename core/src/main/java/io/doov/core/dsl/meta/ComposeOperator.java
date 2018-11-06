/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;

public class ComposeOperator implements Operator {
    private final Operator operator;
    private final LeafPredicateMetadata other;

    public ComposeOperator(Operator operator, LeafPredicateMetadata other) {
        this.operator = operator;
        this.other = other;
    }

    @Override
    public String readable() {
        return  operator.readable() + " " + other.readable();
    }

    @Override
    public String name() {
        return operator.name();
    }
}
