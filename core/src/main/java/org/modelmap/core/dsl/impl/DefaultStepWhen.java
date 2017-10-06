/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.*;

public class DefaultStepWhen implements StepWhen {

    private final StepCondition stepCondition;

    private final String message;

    public DefaultStepWhen(StepCondition stepCondition) {
        this(stepCondition, null);
    }

    private DefaultStepWhen(StepCondition stepCondition, String message) {
        this.stepCondition = stepCondition;
        this.message = message;
    }

    @Override
    public StepWhen withMessage(String message) {
        return new DefaultStepWhen(stepCondition, message);
    }

    @Override
    public StepCondition stepCondition() {
        return stepCondition;
    }

    @Override
    public Optional<String> message() {
        return Optional.ofNullable(message);
    }

    @Override
    public ValidationRule validationRule() {
        return new DefaultValidationRule(this);
    }

    @Override
    public Optional<String> executeOn(FieldModel model) {
        return validationRule().executeOn(model);
    }

    @Override
    public String readable() {
        return stepCondition.readable();
    }

}
