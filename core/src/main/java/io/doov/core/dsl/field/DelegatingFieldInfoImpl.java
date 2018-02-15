/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldInfo;

public class DelegatingFieldInfoImpl<T> implements DelegatingFieldInfo<T> {

    private FieldInfo fieldInfo;

    public DelegatingFieldInfoImpl(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    @Override
    public FieldInfo delegate() {
        return fieldInfo;
    }
}
