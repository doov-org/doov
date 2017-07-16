/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl;

import org.modelmap.core.dsl.impl.DefaultStepWhen;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.lang.StepWhen;

public class DSL {
    public static StepWhen when(StepCondition condition) {
        return new DefaultStepWhen(condition);
    }
}
