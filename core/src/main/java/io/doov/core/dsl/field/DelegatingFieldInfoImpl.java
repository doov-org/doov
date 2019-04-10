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
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.meta.Metadata;

public abstract class DelegatingFieldInfoImpl<T> implements DelegatingFieldInfo<T> {

    private FieldInfo fieldInfo;

    public DelegatingFieldInfoImpl(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    @Override
    public FieldInfo delegate() {
        return fieldInfo;
    }

    @Override
    public FieldId id() {
        return delegate().id();
    }

    @Override
    public Metadata getMetadata() {
        return delegate().getMetadata();
    }

    @Override
    public String readable() {
        return delegate().readable();
    }
}
