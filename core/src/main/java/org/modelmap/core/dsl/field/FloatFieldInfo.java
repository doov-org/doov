/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.FloatCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class FloatFieldInfo extends DefaultFieldInfo<Float> {

    FloatFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    public StepCondition lesserThan(float value) {
        return FloatCondition.lesserThan(this, value);
    }

    public StepCondition lessOrEquals(float value) {
        return FloatCondition.lesserOrEquals(this, value);
    }

    public StepCondition greaterThan(float value) {
        return FloatCondition.greaterThan(this, value);
    }

    public StepCondition greaterOrEquals(float value) {
        return FloatCondition.greaterOrEquals(this, value);
    }

    @Override
    public FloatFieldInfo as(String readable) {
        return new FloatFieldInfo(id(), readable, type(), siblings());
    }

}
