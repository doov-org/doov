package io.doov.core.dsl.impl;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.DefaultConditionalMappingRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.WhenMetadata;

public class DefaultStepWhen extends AbstractDSLBuilder implements StepWhen {
    private final WhenMetadata metadata;
    private final StepCondition stepCondition;

    public DefaultStepWhen(StepCondition stepCondition) {
        this.metadata = WhenMetadata.when(stepCondition.metadata());
        this.stepCondition = stepCondition;
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public StepCondition stepCondition() {
        return stepCondition;
    }

    @Override
    public ValidationRule validate() {
        return new DefaultValidationRule(this);
    }

    @Override
    public ConditionalMappingRule then(MappingRule... mappingRule) {
        return new DefaultConditionalMappingRule(this, mappingRule);
    }
}
