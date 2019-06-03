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
package io.doov.core.dsl;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.DefaultFunction;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.Metadata;

/**
 * Interface for all field types.
 *
 * Generic type parameter {@link T} defines the type of the field.
 */
public interface DslField<T> extends Readable {

    FieldId id();

    /**
     * Returns a new default condition that will use this as a field.
     *
     * @return the default condition
     */
    DefaultFunction<T, ? extends Metadata> getDefaultFunction();

    Metadata getMetadata();
}
