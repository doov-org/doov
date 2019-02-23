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
package io.doov.core.serial;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;

/**
 * Bi-directional mapping to String representation for serialization
 */
public interface StringMapper {

    /**
     * Returns the registry for TypeAdapters
     *
     * @return the type adapter registry
     */
    TypeAdapterRegistry getTypeAdapterRegistry();

    /**
     * Gets the field value as String
     *
     * @param fieldId field id
     * @return field value as string
     */
    String getAsString(FieldId fieldId);

    /**
     * Gets the field value as String
     *
     * @param fieldInfo fieldInfo
     * @return field value as string
     */
    String getAsString(FieldInfo fieldInfo);

    /**
     * Sets the field value from String
     *
     * @param fieldId field id
     * @param value   value as string
     */
    void setAsString(FieldId fieldId, String value);

    /**
     * Sets the field value from String
     *
     * @param fieldInfo field id
     * @param value     value as string
     */
    void setAsString(FieldInfo fieldInfo, String value);
}
