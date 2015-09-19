/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core;

import java.util.*;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

public class DefaultFieldModel implements FieldModel {
    // FIXME separate implementation Wrapper/Map?
    private final Map<FieldId, Object> values = new HashMap<>();
    private final Set<FieldInfo> fieldInfos;

    public DefaultFieldModel() {
        this.fieldInfos = emptySet();
    }

    public DefaultFieldModel(FieldInfo... fieldInfos) {
        this.fieldInfos = newHashSet(fieldInfos);
    }

    @Override
    public Set<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(FieldId fieldId) {
        return (T) values.get(fieldId);
    }

    @Override
    public void set(FieldId fieldId, Object value) {
        values.put(fieldId, value);
        stream(siblingsOf(fieldId)).forEach(s -> values.put(s, value));
    }

    private static final FieldId[] NO_SIBLINGS = new FieldId[]{};

    private FieldId[] siblingsOf(FieldId fieldId) {
        Optional<FieldInfo> sublings = fieldInfos.stream()
                .filter(info -> info.id() == fieldId).findFirst();
        return sublings.isPresent() ? sublings.get().siblings() : NO_SIBLINGS;
    }

    @Override
    public void clear() {
        fieldInfos.stream()
                .filter(info -> get(info.id()) != null)
                .forEach(info -> set(info.id(), null));
    }

    @Override
    public final void clear(TagId tag) {
        fieldInfos.stream()
                .filter(info -> info.id().tags().contains(tag) && get(info.id()) != null)
                .forEach(info -> set(info.id(), null));
    }

    @Override
    public final void setAll(FieldModel context) {
        fieldInfos.stream()
                .filter(info -> context.get(info.id()) != null)
                .forEach(info -> set(info.id(), context.get(info.id())));
    }

    @Override
    public final List<FieldId> getFields() {
        return fieldInfos.stream()
                .filter(info -> get(info.id()) != null)
                .map(FieldInfo::id).collect(toList());
    }

    @Override
    public final List<FieldId> getAllFields() {
        return fieldInfos.stream().map(FieldInfo::id).collect(toList());
    }
}
