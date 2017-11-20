package io.doov.core.dsl.field;

import io.doov.core.dsl.impl.BooleanCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface LogicalFieldInfo extends BaseFieldInfo<Boolean> {

    // not

    default StepCondition not() {
        return getBooleanCondition().not();
    }

    // and

    default StepCondition and(boolean value) {
        return getBooleanCondition().and(value);
    }

    default StepCondition and(LogicalFieldInfo value) {
        return getBooleanCondition().and(value);
    }

    // or

    default StepCondition or(boolean value) {
        return getBooleanCondition().or(value);
    }

    default StepCondition or(LogicalFieldInfo value) {
        return getBooleanCondition().or(value);
    }

    // xor

    default StepCondition xor(boolean value) {
        return getBooleanCondition().xor(value);
    }

    default StepCondition xor(LogicalFieldInfo value) {
        return getBooleanCondition().xor(value);
    }

    // true

    default StepCondition isTrue() {
        return getBooleanCondition().isTrue();
    }

    // false

    default StepCondition isFalse() {
        return getBooleanCondition().isFalse();
    }

    // implementation

    BooleanCondition getBooleanCondition();

}