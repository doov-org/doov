/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static java.text.MessageFormat.format;

import io.doov.core.dsl.lang.*;

public class DefaultStepWhen implements StepWhen {

    private static final String READABLE_WHEN_CONDITION = "When {0}";

    private final StepCondition stepCondition;

    public DefaultStepWhen(StepCondition stepCondition) {
        this.stepCondition = stepCondition;
    }

    @Override
    public StepCondition stepCondition() {
        return stepCondition;
    }

    @Override
    public ValidationRule validate() {
        return new DefaultValidationRule(this);
    }

    @Override
    public String readable() {
        return format(READABLE_WHEN_CONDITION, stepCondition.readable());
    }

}
