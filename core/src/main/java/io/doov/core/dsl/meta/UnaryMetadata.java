/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import io.doov.core.dsl.lang.StepCondition;

public class UnaryMetadata extends AbstractMetadata {

    private final String operator;
    private final StepCondition value;

    private UnaryMetadata(String operator, StepCondition value) {
        this.operator = operator;
        this.value = value;
    }

    public static UnaryMetadata notMetadata(StepCondition value) {
        return new UnaryMetadata("not", value);
    }

    @Override
    public String readable() {
        return "(" + operator + " " + value.readable() + ")";
    }

}
