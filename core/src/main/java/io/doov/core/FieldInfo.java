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

import io.doov.core.dsl.lang.Readable;

/**
 * Properties of a {@code FieldId}, generated from the model java bean
 */
public interface FieldInfo extends Readable {

    /**
     * Returns the referenced {@code FieldId}
     *
     * @return the field id
     */
    FieldId id();

    /**
     * Returns the other {@code FieldId} mapped on the same property
     *
     * @return the siblings array
     */
    FieldId[] siblings();

    /**
     * Returns the {@code FieldId} type
     *
     * @return the field type
     */
    Class<?> type();

    /**
     * Returns the {@code FieldId} type parameters
     *
     * @return the field generic type array
     */
    Class<?>[] genericTypes();

    /**
     * Field metadata for {@code CodeLookup}. This information is used for serialization of {@code Enum} types
     *
     * @return whether {@link CodeLookup} is assignable from the field
     */
    boolean isCodeLookup();

    /**
     * Field metadata for {@code CodeValuable}. This information is used for serialization of {@code Enum} types
     *
     * @return whether {@link CodeValuable} is assignable from the field
     */
    boolean isCodeValuable();

    /**
     * Field metadata for transient. This information can be used to omit the serialization of this field.
     * See {@link FieldTransient} annotation.
     *
     * @return whether the field is transient
     */
    boolean isTransient();

}
