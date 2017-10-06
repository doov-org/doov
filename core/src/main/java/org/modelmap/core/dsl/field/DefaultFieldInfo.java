/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;
import org.modelmap.core.FieldInfo;
import org.modelmap.core.dsl.impl.TypeCondition;
import org.modelmap.core.dsl.lang.StepCondition;

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
    public DefaultFieldInfo<T> as(String readable) {
        return new DefaultFieldInfo<>(fieldId, readable, type, genericTypes, siblings);
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

}
