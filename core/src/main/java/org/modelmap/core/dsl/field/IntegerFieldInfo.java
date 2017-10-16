/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.IntegerCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class IntegerFieldInfo extends DefaultFieldInfo<Integer> implements NumericFieldInfo<Integer> {

    IntegerFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    public StepCondition lesserThan(int value) {
        return new IntegerCondition(this).lesserThan(value);
    }

    public StepCondition lesserOrEquals(int value) {
        return new IntegerCondition(this).lesserOrEquals(value);
    }

    public StepCondition greaterThan(int value) {
        return new IntegerCondition(this).greaterThan(value);
    }

    public StepCondition greaterOrEquals(int value) {
        return new IntegerCondition(this).greaterOrEquals(value);
    }

}
