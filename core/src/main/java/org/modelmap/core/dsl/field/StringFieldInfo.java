/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.StringCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class StringFieldInfo extends DefaultFieldInfo<String> {

    StringFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, String.class, new Class[] {}, siblings);
    }

    public StepCondition contains(String regex) {
        return StringCondition.contains(this, regex);
    }

    public StepCondition matches(String regex) {
        return StringCondition.matches(this, regex);
    }

    public StepCondition startsWith(String regex) {
        return StringCondition.startsWith(this, regex);
    }

    public StepCondition endsWith(String regex) {
        return StringCondition.endsWith(this, regex);
    }

    @Override
    public StringFieldInfo as(String readable) {
        return new StringFieldInfo(id(), readable, siblings());
    }

}
