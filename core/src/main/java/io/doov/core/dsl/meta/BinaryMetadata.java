/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import io.doov.core.dsl.lang.StepCondition;

public class BinaryMetadata extends AbstractMetadata {

    private final StepCondition left;
    private final String operator;
    private final StepCondition right;

    private BinaryMetadata(StepCondition left, String operator, StepCondition right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public static BinaryMetadata andMetadata(StepCondition left, StepCondition right) {
        return new BinaryMetadata(left, "and", right);
    }

    public static BinaryMetadata orMetadata(StepCondition left, StepCondition right) {
        return new BinaryMetadata(left, "or", right);
    }

    @Override
    public String readable() {
        return "(" + (left.readable() + " " + operator + " " + right.readable()) + ")";
    }

}
