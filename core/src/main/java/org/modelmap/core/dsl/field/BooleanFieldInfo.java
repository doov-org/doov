/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.BooleanCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class BooleanFieldInfo extends DefaultFieldInfo<Boolean> {

    BooleanFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    public StepCondition isTrue() {
        return BooleanCondition.isTrue(this);
    }

    public StepCondition isFalse() {
        return BooleanCondition.isFalse(this);
    }

}
