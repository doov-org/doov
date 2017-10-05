/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.BooleanCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class BooleanFieldInfo extends DefaultFieldInfo<Boolean> {

    BooleanFieldInfo(FieldId fieldId, Class<?> type, FieldId[] siblings) {
        super(fieldId, type, new Class[] {}, siblings);
    }

    public StepCondition isTrue(boolean value) {
        return BooleanCondition.isTrue(this, value);
    }

    public StepCondition isFalse(boolean value) {
        return BooleanCondition.isFalse(this, value);
    }

}
