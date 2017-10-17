/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.FloatCondition;

public class FloatFieldInfo extends DefaultFieldInfo<Float> implements NumericFieldInfo<FloatFieldInfo, Float> {

    FloatFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    @Override
    public FloatCondition getNumericCondition() {
        return new FloatCondition(this);
    }

}
