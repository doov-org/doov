package org.modelmap.core.dsl.impl;

import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.StepCondition;

public class DefaultStepCondition implements StepCondition {
    private final Predicate<FieldModel> predicate;

    public DefaultStepCondition(Predicate<FieldModel> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Predicate<FieldModel> predicate() {
        return predicate;
    }

    @Override
    public StepCondition and(StepCondition other) {
        return new DefaultStepCondition(predicate.and(other.predicate()));
    }

    @Override
    public StepCondition andNot(StepCondition other) {
        return new DefaultStepCondition(predicate.and(other.predicate().negate()));
    }

    @Override
    public StepCondition or(StepCondition other) {
        return new DefaultStepCondition(predicate.or(other.predicate()));
    }

    @Override
    public StepCondition orNot(StepCondition other) {
        return new DefaultStepCondition(predicate.or(other.predicate().negate()));
    }

    @Override
    public StepCondition not() {
        return new DefaultStepCondition(predicate.negate());
    }
}
