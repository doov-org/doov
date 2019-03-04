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
package io.doov.core.dsl.impl;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.serial.TypeAdapterRegistry;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Stream;

public final class ModelInterceptor implements FieldModel {
    private final FieldModel model;
    private final Context context;

    public ModelInterceptor(FieldModel model, Context context) {
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

    @Override
    public TypeAdapterRegistry getTypeAdapterRegistry() {
        return model.getTypeAdapterRegistry();
    }
}
