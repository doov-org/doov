/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl;

import org.modelmap.core.dsl.impl.DefaultStepWhen;
import org.modelmap.core.dsl.impl.LogicalNaryCondition;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.lang.StepWhen;

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
