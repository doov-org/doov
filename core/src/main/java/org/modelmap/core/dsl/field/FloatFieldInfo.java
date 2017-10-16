/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.FloatCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class FloatFieldInfo extends DefaultFieldInfo<Float> implements NumericFieldInfo<Float> {

    FloatFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    public StepCondition lesserThan(float value) {
        return new FloatCondition(this).lesserThan(value);
    }

    public StepCondition lesserOrEquals(float value) {
        return new FloatCondition(this).lesserOrEquals(value);
    }

    public StepCondition greaterThan(float value) {
        return new FloatCondition(this).greaterThan(value);
    }

    public StepCondition greaterOrEquals(float value) {
        return new FloatCondition(this).greaterOrEquals(value);
    }

    public StepCondition between(float minIncluded, float maxExcluded) {
        return new FloatCondition(this).between(minIncluded, maxExcluded);
    }

}
