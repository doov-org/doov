/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core;

import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import java.util.*;

public class DefaultFieldModel implements FieldModel {

    private final Map<FieldId, Object> values = new HashMap<>();
    private final Set<FieldInfo> fieldInfos;

    public DefaultFieldModel() {
        this.fieldInfos = emptySet();
    }

    public DefaultFieldModel(FieldInfo... fieldInfos) {
        this.fieldInfos = stream(fieldInfos).collect(toSet());
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

    private static final FieldId[] NO_SIBLINGS = new FieldId[] { };

    private FieldId[] siblingsOf(FieldId fieldId) {
        Optional<FieldInfo> sublings = fieldInfos.stream()
                        .filter(info -> info.id() == fieldId).findFirst();
        return sublings.isPresent() ? sublings.get().siblings() : NO_SIBLINGS;
    }
}
