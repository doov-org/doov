/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import static java.text.MessageFormat.format;

import org.modelmap.core.dsl.lang.*;

public class DefaultStepWhen implements StepWhen {

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
        return new DefaultStepValidate(this);
    }

    @Override
    public String readable() {
        return format("When {0}", stepCondition.readable());
    }

}
