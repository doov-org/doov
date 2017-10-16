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
        return DoubleCondition.lesserThan(this, value);
    }

    public StepCondition lesserOrEquals(double value) {
        return DoubleCondition.lesserOrEquals(this, value);
    }

    public StepCondition greaterThan(double value) {
        return DoubleCondition.greaterThan(this, value);
    }

    public StepCondition greaterOrEquals(double value) {
        return DoubleCondition.greaterOrEquals(this, value);
    }

}
