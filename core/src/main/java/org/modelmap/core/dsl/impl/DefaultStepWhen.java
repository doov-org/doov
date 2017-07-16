/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.lang.StepThrowMessage;
import org.modelmap.core.dsl.lang.StepWhen;

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
    public StepThrowMessage throwMessage(String message) {
        return new DefaultStepThrowMessage(this, message);
    }
}
