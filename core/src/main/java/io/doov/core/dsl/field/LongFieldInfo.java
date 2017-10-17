/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.LongCondition;

public class LongFieldInfo extends DefaultFieldInfo<Long> implements NumericFieldInfo<LongFieldInfo, Long> {

    LongFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    @Override
    public LongCondition getNumericCondition() {
        return new LongCondition(this);
    }

}
