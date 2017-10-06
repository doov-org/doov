/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.StepWhen;
import org.modelmap.core.dsl.lang.ValidationRule;

public class DefaultValidationRule implements ValidationRule {

    private final StepWhen stepThrowMessage;

    public DefaultValidationRule(StepWhen stepThrowMessage) {
        this.stepThrowMessage = stepThrowMessage;
    }

    @Override
    public Optional<String> executeOn(FieldModel model) {
        return stepThrowMessage.stepCondition().predicate().test(model)
                        ? Optional.of(stepThrowMessage.message().orElse(stepThrowMessage.readable()))
                        : Optional.empty();
    }

    @Override
    public String readable() {
        return stepThrowMessage.readable();
    }

}
