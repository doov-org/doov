/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import java.util.Optional;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.IntegerCondition;
import org.modelmap.core.dsl.impl.StringCondition;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.meta.FieldMetadata;

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

    public StepCondition startsWith(String prefix) {
        return StringCondition.startsWith(this, prefix);
    }

    public StepCondition endsWith(String suffix) {
        return StringCondition.endsWith(this, suffix);
    }

    public IntegerCondition length() {
        return new IntegerCondition(FieldMetadata.length(this),
                        fieldModel -> Optional.ofNullable(fieldModel.<String> get(this.id())).map(String::length));
    }

}
