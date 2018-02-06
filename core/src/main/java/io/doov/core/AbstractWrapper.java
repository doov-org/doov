/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core;

import java.util.List;

public abstract class AbstractWrapper<M> implements FieldModel {

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

}
