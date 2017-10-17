/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.IntegerCondition;

public class IntegerFieldInfo extends DefaultFieldInfo<Integer> implements NumericFieldInfo<IntegerFieldInfo, Integer> {

    IntegerFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    @Override
    public IntegerCondition getNumericCondition() {
        return new IntegerCondition(this);
    }

}
