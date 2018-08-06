/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import io.doov.core.FieldId;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;

public final class ModelInterceptor implements DslModel {
    private final DslModel model;
    private final Context context;

    public ModelInterceptor(DslModel model, Context context) {
        this.model = model;
        this.context = context;
    }

    @Override
    public <T> T get(FieldId id) {
        final T value = model.get(id);
        context.addEvalValue(id, value);
        return value;
    }

    @Override
    public <T> void set(FieldId fieldId, T value) {
        model.set(fieldId, value);
        context.addSetValue(fieldId, value);
    }
}
