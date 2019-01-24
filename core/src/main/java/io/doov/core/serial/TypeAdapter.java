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

import io.doov.core.FieldInfo;

/**
 *
 */
public interface TypeAdapter {

    /**
     * Called before {@link #fromString(FieldInfo, String)}
     *
     * @param info field info
     * @return true if this type adapter accepts to deserialize this field
     */
    boolean accept(FieldInfo info);

    /**
     * Called before {@link #toString(Object)}
     *
     * @param value value
     * @return true if this type adapter accepts to serialize this object
     */
    boolean accept(Object value);

    /**
     * Serialize
     *
     * @param value object
     * @return string representation of the object
     */
    String toString(Object value);

    /**
     * Deserialize
     *
     * @param info  field info
     * @param value string value
     * @return object value
     */
    Object fromString(FieldInfo info, String value);
}
