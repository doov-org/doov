/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import java.time.temporal.Temporal;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.dsl.lang.StepCondition;

interface TemporalFieldInfo<T extends Temporal> extends FieldInfo {

    StepCondition eq(T value);

    StepCondition isBefore(T value);

    StepCondition isAfter(T value);

    default StepCondition between(T minValue, T maxValue) {
        return isBeforeOrEq(maxValue).and(isAfterOrEq(minValue));
    }

    default StepCondition notBetween(T minValue, T maxValue) {
        return between(minValue, maxValue).not();
    }

    default StepCondition isBeforeOrEq(T value) {
        return isBefore(value).or(eq(value));
    }

    default StepCondition isAfterOrEq(T value) {
        return isAfter(value).or(eq(value));
    }
}
