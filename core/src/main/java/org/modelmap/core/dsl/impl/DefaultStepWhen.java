/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

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
    public StepValidate validate() {
        return new DefaultStepValidate(this);
    }

    @Override
    public String readable() {
        return stepCondition.readable();
    }

}
