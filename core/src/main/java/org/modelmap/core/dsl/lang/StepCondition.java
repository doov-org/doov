package org.modelmap.core.dsl.lang;

import java.util.function.Predicate;

import org.modelmap.core.FieldModel;

public interface StepCondition {
    Predicate<FieldModel> predicate();

    StepCondition and(StepCondition other);

    StepCondition andNot(StepCondition other);

    StepCondition or(StepCondition other);

    StepCondition orNot(StepCondition other);

    StepCondition not();
}
