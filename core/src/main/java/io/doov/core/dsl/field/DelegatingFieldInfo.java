/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.impl.DefaultCondition;

public class DelegatingFieldInfo<T> implements FieldInfo, BaseFieldInfo<T> {

    private FieldInfo fieldInfo;

    public DelegatingFieldInfo(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    @Override
    public DefaultCondition<T> getDefaultCondition() {
        return new DefaultCondition<>(this);
    }

    @Override
    public FieldId id() {
        return fieldInfo.id();
    }

    @Override
    public String readable() {
        return fieldInfo.readable();
    }

    @Override
    public FieldId[] siblings() {
        return fieldInfo.siblings();
    }

    @Override
    public Class<?> type() {
        return fieldInfo.type();
    }

    @Override
    public Class<?>[] genericTypes() {
        return fieldInfo.genericTypes();
    }

    @Override
    public boolean isCodeLookup() {
        return fieldInfo.isCodeLookup();
    }

    @Override
    public boolean isCodeValuable() {
        return fieldInfo.isCodeValuable();
    }

    @Override
    public boolean isTransient() {
        return fieldInfo.isTransient();
    }
}
