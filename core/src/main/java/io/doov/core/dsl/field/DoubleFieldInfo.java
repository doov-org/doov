/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.DoubleCondition;
import io.doov.core.dsl.lang.StepCondition;

public class DoubleFieldInfo extends DefaultFieldInfo<Double> implements NumericFieldInfo<Double> {

    DoubleFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    public StepCondition lesserThan(double value) {
        return new DoubleCondition(this).lesserThan(value);
    }

    public StepCondition lesserOrEquals(double value) {
        return new DoubleCondition(this).lesserOrEquals(value);
    }

    public StepCondition greaterThan(double value) {
        return new DoubleCondition(this).greaterThan(value);
    }

    public StepCondition greaterOrEquals(double value) {
        return new DoubleCondition(this).greaterOrEquals(value);
    }

    public StepCondition between(double minIncluded, double maxExcluded) {
        return new DoubleCondition(this).between(minIncluded, maxExcluded);
    }

}
