/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.DoubleCondition;
import org.modelmap.core.dsl.lang.StepCondition;

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
