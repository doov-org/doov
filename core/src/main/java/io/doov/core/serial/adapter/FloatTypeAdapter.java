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
package io.doov.core.serial.adapter;

import io.doov.core.FieldInfo;
import io.doov.core.serial.TypeAdapter;

public class FloatTypeAdapter implements TypeAdapter {

    @Override
    public boolean accept(FieldInfo info) {
        return Float.class.equals(info.type()) || Float.TYPE.equals(info.type());
    }

    @Override
    public boolean accept(Object value) {
        return value instanceof Float;
    }

    @Override
    public String toString(Object value) {
        return String.valueOf(value);
    }

    @Override
    public Object fromString(FieldInfo info, String value) {
        return Float.parseFloat(value);
    }
}
