/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.IntegerCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class IntegerFieldInfo extends DefaultFieldInfo<Integer> {

    IntegerFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    public StepCondition lesserThan(int value) {
        return IntegerCondition.lesserThan(this, value);
    }

    public StepCondition lesserOrEquals(int value) {
        return IntegerCondition.lesserOrEquals(this, value);
    }

    public StepCondition greaterThan(int value) {
        return IntegerCondition.greaterThan(this, value);
    }

    public StepCondition greaterOrEquals(int value) {
        return IntegerCondition.greaterOrEquals(this, value);
    }

    @Override
    public IntegerFieldInfo as(String readable) {
        return new IntegerFieldInfo(id(), readable, type(), siblings());
    }

}
