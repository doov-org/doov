/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.IntegerCondition;
import io.doov.core.dsl.lang.StepCondition;

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

    public StepCondition between(int minIncluded, int maxExcluded) {
        return new IntegerCondition(this).between(minIncluded, maxExcluded);
    }

}
