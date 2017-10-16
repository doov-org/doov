package org.modelmap.core.dsl.impl;

import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.meta.Metadata;

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
