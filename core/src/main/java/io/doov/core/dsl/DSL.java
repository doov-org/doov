/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl;

import io.doov.core.dsl.impl.DefaultStepWhen;
import io.doov.core.dsl.impl.LogicalNaryCondition;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.StepWhen;

public class DSL {

    public static StepWhen when(StepCondition condition) {
        return new DefaultStepWhen(condition);
    }

    public static StepCondition matchAny(StepCondition... steps) {
        return LogicalNaryCondition.matchAny(steps);
    }

    public static StepCondition matchAll(StepCondition... steps) {
        return LogicalNaryCondition.matchAll(steps);
    }

    public static StepCondition matchNone(StepCondition... steps) {
        return LogicalNaryCondition.matchNone(steps);
    }

}
