/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.*;
import java.util.stream.Stream;

import io.doov.core.*;
import io.doov.core.dsl.DslModel;
import io.doov.core.serial.TypeAdapterRegistry;

public class TemplateModel implements FieldModel {

    private final DslModel model;
    private final Map<FieldId,FieldId> resolutions;

    public TemplateModel(DslModel model, Map<FieldId,FieldId> resolutions) {
        this.model = model;
        this.resolutions = resolutions;
    }

    @Override
    public <T> T get(FieldId id) {
        return model.get(resolutions.getOrDefault(id, id));
    }

    @Override
    public TypeAdapterRegistry getTypeAdapterRegistry() {
        return model.getTypeAdapterRegistry();
    }

    @Override
    public <T> void set(FieldId fieldId, T value) {
        model.set(resolutions.getOrDefault(fieldId,fieldId),value);
    }

    @Override
    public Stream<Map.Entry<FieldId, Object>> stream() {
        return model.stream();
    }

    @Override
    public Iterator<Map.Entry<FieldId, Object>> iterator() {
        return model.iterator();
    }

    @Override
    public Spliterator<Map.Entry<FieldId, Object>> spliterator() {
        return model.spliterator();
    }

    @Override
    public Stream<Map.Entry<FieldId, Object>> parallelStream() {
        return model.parallelStream();
    }

    @Override
    public List<FieldInfo> getFieldInfos() {
        return model.getFieldInfos();
    }
}
