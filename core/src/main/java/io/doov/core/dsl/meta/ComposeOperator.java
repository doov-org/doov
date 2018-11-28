/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

public class ComposeOperator implements Operator {
    private final Operator operator;
    private final LeafMetadata<?> other;

    public ComposeOperator(Operator operator, LeafMetadata<?> other) {
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
