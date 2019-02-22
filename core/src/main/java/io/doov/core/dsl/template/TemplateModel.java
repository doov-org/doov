/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.Map;

import io.doov.core.FieldId;
import io.doov.core.dsl.DslModel;

public class TemplateModel implements DslModel {

    private DslModel model;
    private Map<FieldId,FieldId> resolutions;

    public TemplateModel(DslModel model, Map<FieldId,FieldId> resolutions) {
        this.model = model;
        this.resolutions = resolutions;
    }

    @Override
    public <T> T get(FieldId id) {
        return model.get(resolutions.getOrDefault(id, id));
    }

    @Override
    public <T> void set(FieldId fieldId, T value) {
        model.set(resolutions.getOrDefault(fieldId,fieldId),value);
    }
}
