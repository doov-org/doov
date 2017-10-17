package io.doov.core.dsl.impl;

import java.util.function.Predicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;

abstract class AbstractStepCondition implements StepCondition {

    final Metadata metadata;
    final Predicate<FieldModel> predicate;

    AbstractStepCondition(Metadata metadata, Predicate<FieldModel> predicate) {
        this.metadata = metadata;
        this.predicate = predicate;
    }

    @Override
    public Predicate<FieldModel> predicate() {
        return predicate;
    }

    @Override
    public String readable() {
        return metadata.readable();
    }

}
