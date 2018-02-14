/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core;

import java.util.*;

import io.doov.core.serial.*;

public abstract class AbstractWrapper<M> implements FieldModel, StringMapper {

    protected final List<FieldInfo> fieldInfos;
    protected final M model;

    public AbstractWrapper(List<FieldInfo> fieldInfos, M model) {
        this.fieldInfos = fieldInfos;
        this.model = model;
    }

    public M getModel() {
        return model;
    }

    @Override
    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    protected abstract TypeAdapterRegistry getTypeAdapterRegistry();

    @Override
    public String getAsString(FieldId fieldId) {
        Object value = get(fieldId);
        if (value == null) {
            return null;
        }
        return getTypeAdapterRegistry().stream()
                        .filter(a -> a.accept(value))
                        .findFirst()
                        .map(a -> a.toString(value))
                        .orElse(null);
    }

    @Override
    public String getAsString(FieldInfo info) {
        Objects.requireNonNull(info);
        return getAsString(info.id());
    }

    @Override
    public void setAsString(FieldId fieldId, String value) {
        FieldInfo fieldInfo = info(fieldId);
        setAsString(fieldInfo, value);
    }

    @Override
    public void setAsString(FieldInfo fieldInfo, String value) {
        Objects.requireNonNull(fieldInfo);
        if (value == null) {
            set(fieldInfo.id(), null);
        } else {
            set(fieldInfo.id(), getTypeAdapterRegistry().stream()
                            .filter(a -> a.accept(fieldInfo))
                            .findFirst()
                            .map(a -> a.fromString(fieldInfo, value))
                            .orElseThrow(() -> new IllegalStateException("cannot set field "
                                            + fieldInfo.id() + " with value " + value)));
        }
    }

}
