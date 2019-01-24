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

/**
 * Delegates all FieldInfo methods to another.
 */
public interface DelegatingFieldInfo extends FieldInfo {

    FieldInfo delegate();

    @Override
    default FieldId id() {
        return delegate().id();
    }

    @Override
    default String readable() {
        return delegate().readable();
    }

    @Override
    default FieldId[] siblings() {
        return delegate().siblings();
    }

    @Override
    default Class<?> type() {
        return delegate().type();
    }

    @Override
    default Class<?>[] genericTypes() {
        return delegate().genericTypes();
    }

    @Override
    default boolean isCodeLookup() {
        return delegate().isCodeLookup();
    }

    @Override
    default boolean isCodeValuable() {
        return delegate().isCodeValuable();
    }

    @Override
    default boolean isTransient() {
        return delegate().isTransient();
    }
}
