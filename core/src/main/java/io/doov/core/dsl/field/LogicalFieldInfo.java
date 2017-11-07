package io.doov.core.dsl.field;

import io.doov.core.dsl.impl.BooleanCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface LogicalFieldInfo {

    default StepCondition isTrue() {
        return getBooleanCondition().isTrue();
    }

    default StepCondition isFalse() {
        return getBooleanCondition().isFalse();
    }

    BooleanCondition getBooleanCondition();

}
