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
