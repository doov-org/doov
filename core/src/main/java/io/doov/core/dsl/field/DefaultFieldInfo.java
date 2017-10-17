/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.impl.TypeCondition;
import io.doov.core.dsl.lang.StepCondition;

public class DefaultFieldInfo<T> implements FieldInfo {

    private final FieldId fieldId;
    private final String readable;
    private final Class<?> type;
    private final Class<?>[] genericTypes;
    private final FieldId[] siblings;

    DefaultFieldInfo(FieldId fieldId, String readable, Class<?> type, Class<?>[] genericTypes, FieldId... siblings) {
        this.fieldId = fieldId;
        this.readable = readable;
        this.type = type;
        this.genericTypes = genericTypes;
        this.siblings = siblings;
    }

    @Override
    public FieldId id() {
        return fieldId;
    }

    @Override
    public String readable() {
        return readable;
    }

    @Override
    public Class<?> type() {
        return type;
    }

    @Override
    public FieldId[] siblings() {
        return siblings;
    }

    @Override
    public Class<?>[] genericTypes() {
        return genericTypes;
    }

    public StepCondition eq(T value) {
        return TypeCondition.eq(this, value);
    }

    public StepCondition notEq(T value) {
        return TypeCondition.notEq(this, value);
    }

    public StepCondition isNull() {
        return TypeCondition.isNull(this);
    }

    public StepCondition isNotNull() {
        return TypeCondition.isNotNull(this);
    }

}
