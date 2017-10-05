/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.*;

public class DefaultStepThrowMessage implements StepThrowMessage {

    private final StepWhen stepWhen;
    private final String message;

    public DefaultStepThrowMessage(StepWhen stepWhen, String message) {
        this.stepWhen = stepWhen;
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public StepWhen stepWhen() {
        return stepWhen;
    }

    @Override
    public void executeOn(FieldModel model) {
        validationRule().executeOn(model);
    }

    @Override
    public ValidationRule validationRule() {
        return new DefaultValidationRule(this);
    }

    @Override
    public String readable() {
        return stepWhen.readable();
    }

}
