/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.DoubleCondition;

public class DoubleFieldInfo extends DefaultFieldInfo<Double> implements NumericFieldInfo<DoubleFieldInfo, Double> {

    DoubleFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    @Override
    public DoubleCondition getNumericCondition() {
        return new DoubleCondition(this);
    }

}
