/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import static io.doov.core.dsl.meta.FieldMetadata.lengthIsMetadata;

import java.util.Optional;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.IntegerCondition;
import io.doov.core.dsl.impl.StringCondition;
import io.doov.core.dsl.lang.StepCondition;

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
        return new IntegerCondition(lengthIsMetadata(this),
                        fieldModel -> Optional.ofNullable(fieldModel.<String> get(this.id())).map(String::length));
    }

}
