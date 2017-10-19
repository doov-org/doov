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
        return new StringCondition(this).contains(regex);
    }

    public StepCondition matches(String regex) {
        return new StringCondition(this).matches(regex);
    }

    public StepCondition startsWith(String prefix) {
        return new StringCondition(this).startsWith(prefix);
    }

    public StepCondition endsWith(String suffix) {
        return new StringCondition(this).endsWith(suffix);
    }

    public IntegerCondition length() {
        return new IntegerCondition(lengthIsMetadata(this),
                        fieldModel -> Optional.ofNullable(fieldModel.<String> get(this.id())).map(String::length));
    }

}
