/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import io.doov.core.dsl.lang.StepCondition;

public class UnaryMetadata extends AbstractMetadata {

    public static final String NOT = "not";

    public final String operator;
    public final StepCondition value;

    public UnaryMetadata(String operator, StepCondition value) {
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String readable() {
        return "(" + operator + " " + value.readable() + ")";
    }

}
