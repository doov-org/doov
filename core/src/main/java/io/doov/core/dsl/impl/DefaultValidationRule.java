package io.doov.core.dsl.impl;

import static java.text.MessageFormat.format;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;

public class DefaultValidationRule implements ValidationRule {

    private static final String READABLE_VALIDATE_WITH_EMPTY_MESSAGE = "{0}, validate with empty message";
    private static final String READABLE_VALIDATE_WITH_MESSAGE = "{0}, validate with message \"{1}\"";

    private final StepWhen stepWhen;
    private final String message;

    DefaultValidationRule(StepWhen stepWhen) {
        this(stepWhen, null);
    }

    private DefaultValidationRule(StepWhen stepWhen, String message) {
        this.stepWhen = stepWhen;
        this.message = message;
    }

    @Override
    public ValidationRule withMessage(String message) {
        return new DefaultValidationRule(stepWhen, message);
    }

    @Override
    public String readable() {
        String pattern = message == null ? READABLE_VALIDATE_WITH_EMPTY_MESSAGE : READABLE_VALIDATE_WITH_MESSAGE;
        return format(pattern, stepWhen.readable(), message);
    }

    @Override
    public Result executeOn(FieldModel model) {
        boolean valid = stepWhen.stepCondition().predicate().test(model);
        String readable = valid ? null : (message == null ? stepWhen.stepCondition().readable() : message);
        return new DefaultResult(valid, readable);
    }

    @Override
    public ValidationRule registerOn(RuleRegistry registry) {
        registry.register(this);
        return this;
    }

    @Override
    public ValidationRule registerOn(RuleRegistry registry, RuleId id) {
        registry.register(this, id);
        return this;
    }

}
