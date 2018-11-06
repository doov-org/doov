package io.doov.core.dsl.impl;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.RuleMetadata;

public class DefaultValidationRule extends AbstractDSLBuilder implements ValidationRule {

    private final RuleMetadata metadata;
    private final StepWhen stepWhen;
    private final boolean shortCircuit;

    public DefaultValidationRule(StepWhen stepWhen) {
        this(stepWhen, true);
    }

    public DefaultValidationRule(StepWhen stepWhen, boolean shortCircuit) {
        this.metadata = RuleMetadata.rule(stepWhen.metadata());
        this.stepWhen = stepWhen;
        this.shortCircuit = shortCircuit;
    }

    protected boolean isShortCircuit() {
        return shortCircuit;
    }

    protected StepWhen getStepWhen() {
        return stepWhen;
    }

    @Override
    public ValidationRule withShortCircuit(boolean shortCircuit) {
        return new DefaultValidationRule(stepWhen, shortCircuit);
    }

    @Override
    public Result executeOn(DslModel model) {
        return executeOn(model, new DefaultContext(shortCircuit, stepWhen.stepCondition().metadata()));
    }

    @Override
    public Result executeOn(DslModel model, Context context) {
        boolean valid = stepWhen.stepCondition().predicate().test(model, context);
        return new DefaultResult(valid, context);
    }

    @Override
    public ValidationRule registerOn(RuleRegistry registry) {
        registry.register(this);
        return this;
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

}
