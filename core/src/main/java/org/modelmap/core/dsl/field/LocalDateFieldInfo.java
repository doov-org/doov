/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import java.time.LocalDate;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.LocalDateCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class LocalDateFieldInfo extends DefaultFieldInfo<LocalDate> {

    LocalDateFieldInfo(FieldId fieldId, FieldId[] siblings) {
        super(fieldId, LocalDate.class, new Class[] {}, siblings);
    }

    public StepCondition before(LocalDate value) {
        return LocalDateCondition.before(this, value);
    }

    public StepCondition beforeOrEq(LocalDate value) {
        return LocalDateCondition.beforeOrEq(this, value);
    }

    public StepCondition after(LocalDate value) {
        return LocalDateCondition.after(this, value);
    }

    public StepCondition afterOrEq(LocalDate value) {
        return LocalDateCondition.afterOrEq(this, value);
    }

    public StepCondition between(LocalDate minValue, LocalDate maxValue) {
        return LocalDateCondition.between(this, minValue, maxValue);
    }

    public StepCondition notBetween(LocalDate minValue, LocalDate maxValue) {
        return LocalDateCondition.notBetween(this, minValue, maxValue);
    }

}
