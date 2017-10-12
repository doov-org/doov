/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.LongCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class LongFieldInfo extends DefaultFieldInfo<Long> {

    LongFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    public StepCondition lesserThan(long value) {
        return LongCondition.lesserThan(this, value);
    }

    public StepCondition lesserOrEquals(long value) {
        return LongCondition.lesserOrEquals(this, value);
    }

    public StepCondition greaterThan(long value) {
        return LongCondition.greaterThan(this, value);
    }

    public StepCondition greaterOrEquals(long value) {
        return LongCondition.greaterOrEquals(this, value);
    }

}
