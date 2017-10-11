package org.modelmap.core.dsl.impl;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.*;

public class DefaultStepValidate implements StepValidate {

    private final StepWhen stepWhen;
    private final String message;

    public DefaultStepValidate(StepWhen stepWhen) {
        this(stepWhen, null);
    }

    public DefaultStepValidate(StepWhen stepWhen, String message) {
        this.stepWhen = stepWhen;
        this.message = message;
    }

    @Override
    public StepValidate withMessage(String message) {
        return new DefaultStepValidate(stepWhen, message);
    }

    @Override
    public String readable() {
        return stepWhen.readable();
    }

    @Override
    public Result executeOn(FieldModel model) {
        boolean valid = stepWhen.stepCondition().predicate().test(model);
        String readable = valid ? null : (message == null ? readable() : message);
        EValidity status = valid ? EValidity.VALID : EValidity.INVALID;
        return new DefaultResult(status, readable);
    }

}
