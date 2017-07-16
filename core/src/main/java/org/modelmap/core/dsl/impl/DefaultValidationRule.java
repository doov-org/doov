/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.StepThrowMessage;
import org.modelmap.core.dsl.lang.ValidationRule;

public class DefaultValidationRule implements ValidationRule {
    private final StepThrowMessage stepThrowMessage;

    public DefaultValidationRule(StepThrowMessage stepThrowMessage) {
        this.stepThrowMessage = stepThrowMessage;
    }

    @Override
    public void executeOn(FieldModel model) {
        if (stepThrowMessage.stepWhen().stepCondition().predicate().test(model))
            throw new RuntimeException(stepThrowMessage.message());
    }

}
