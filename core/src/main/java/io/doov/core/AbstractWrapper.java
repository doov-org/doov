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
package io.doov.core;

import java.util.List;

/**
 * Base class for Wrapper implementation.
 *
 * @param <M> model type
 */
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
