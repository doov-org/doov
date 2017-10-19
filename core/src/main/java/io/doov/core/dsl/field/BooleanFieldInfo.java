/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.BooleanCondition;
import io.doov.core.dsl.lang.StepCondition;

public class BooleanFieldInfo extends DefaultFieldInfo<Boolean> {

    BooleanFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    public StepCondition isTrue() {
        return new BooleanCondition(this).isTrue();
    }

    public StepCondition isFalse() {
        return new BooleanCondition(this).isFalse();
    }

}
