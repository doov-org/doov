/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl.runtime;

import java.util.*;
import java.util.stream.Stream;

import io.doov.core.*;
import io.doov.core.serial.TypeAdapterRegistry;
import io.doov.core.serial.TypeAdapters;

/**
 * Runtime implementation of {@link FieldModel}
 * Delegates FieldInfo search operations to {@link RuntimeFieldRegistry}
 *
 * @param <M> model entry type
 */
public class RuntimeModel<M> implements FieldModel {

    private RuntimeFieldRegistry<M> fieldRegistry;
    private M model;

    public RuntimeModel(RuntimeFieldRegistry<M> fieldRegistry, M model) {
        this.fieldRegistry = fieldRegistry;
        this.model = model;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(FieldId id) {
        RuntimeField<M, Object> runtimeField = fieldRegistry.get(id);
        if (runtimeField == null) {
            return null;
        }
        return (T) runtimeField.get(model);
    }

    @Override
    public <T> void set(FieldId fieldId, T value) {
        RuntimeField<M, Object> runtimeField = fieldRegistry.get(fieldId);
        if (runtimeField != null) {
            runtimeField.set(model, value);
        }
    }

    @Override
    public Stream<Map.Entry<FieldId, Object>> stream() {
        return fieldRegistry.stream().map(e -> new SupplierEntry<>(e, model));
    }

    @Override
    public Iterator<Map.Entry<FieldId, Object>> iterator() {
        return new Iterator<Map.Entry<FieldId, Object>>() {
            int position = 0;

            @Override
            public boolean hasNext() {
                return position < fieldRegistry.runtimeFields().size();
            }

            @Override
            public Map.Entry<FieldId, Object> next() {
                try {
                    RuntimeField<M, Object> mObjectRuntimeField = fieldRegistry.runtimeFields().get(position);
                    return new SupplierEntry<>(mObjectRuntimeField, model);
                } finally {
                    position++;
                }
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Spliterator<Map.Entry<FieldId, Object>> spliterator() {
        Map.Entry<FieldId, Object>[] entries = new Map.Entry[fieldRegistry.runtimeFields().size()];
        for (int i = 0; i < fieldRegistry.runtimeFields().size(); i++) {
            RuntimeField<M, Object> runtimeField = fieldRegistry.runtimeFields().get(i);
            entries[i] = new SupplierEntry<>(runtimeField, model);
        }
        return Arrays.spliterator(entries, 0, entries.length);
    }

    @Override
    public Stream<Map.Entry<FieldId, Object>> parallelStream() {
        return fieldRegistry.stream().parallel().map(e -> new SupplierEntry<>(e, model));
    }

    @Override
    public List<FieldInfo> getFieldInfos() {
        return fieldRegistry.fieldInfos();
    }

    private static class SupplierEntry<M> implements Map.Entry<FieldId, Object> {

        final RuntimeField<M, Object> runtimeField;
        final M model;

        SupplierEntry(RuntimeField<M, Object> runtimeField, M model) {
            super();
            this.runtimeField = runtimeField;
            this.model = model;
        }

        @Override
        public FieldId getKey() {
            return runtimeField.id();
        }

        @Override
        public Object getValue() {
            return runtimeField.apply(model);
        }

        @Override
        public Object setValue(Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return runtimeField.id() + ", " + String.valueOf(getValue());
        }
    }

    @Override
    public TypeAdapterRegistry getTypeAdapterRegistry() {
        return TypeAdapters.INSTANCE;
    }

}
