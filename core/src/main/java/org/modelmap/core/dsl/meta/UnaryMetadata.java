/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.meta;

import org.modelmap.core.dsl.lang.StepCondition;

public class UnaryMetadata implements Readable {

    public static final String NOT = "not";

    public final StepCondition value;
    public final String operator;

    public UnaryMetadata(StepCondition value, String operator) {
        this.value = value;
        this.operator = operator;
    }

    @Override
    public String readable() {
        return "(" + operator + " " + value.readable() + ")";
    }

}
