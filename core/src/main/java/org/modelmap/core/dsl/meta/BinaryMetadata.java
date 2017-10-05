/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.meta;

import org.modelmap.core.dsl.lang.StepCondition;

public class BinaryMetadata implements Readable {

    public static final String AND = "and";
    public static final String OR = "or";

    public final StepCondition left;
    public final String operator;
    public final StepCondition right;

    public BinaryMetadata(StepCondition left, String operator, StepCondition right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String readable() {
        return "(" + left.readable() + " " + operator + " " + right.readable() + ")";
    }

}
