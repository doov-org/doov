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

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import io.doov.core.FieldInfo;

/**
 * Provider interface for FieldInfo class types.
 */
public interface FieldTypeProvider {

    /**
     * @param fieldInfo untyped field info
     * @return class type for typed field info implementation class
     */
    default Class<? extends FieldInfo> fielInfoType(FieldInfo fieldInfo) {
        Optional<? extends Class<? extends FieldInfo>> first = getTypes().entrySet().stream()
                        .filter(e -> e.getKey().test(fieldInfo))
                        .map(Map.Entry::getValue)
                        .findFirst();
        return first.isPresent() ? first.get() : getDefaultFieldInfoClass();
    }

    /**
     * @return class type for default field info implementation
     */
    default Class<? extends FieldInfo> getDefaultFieldInfoClass() {
        return DefaultFieldInfo.class;
    }

    /**
     * To implement by the implementors of this interface
     *
     * @return immutable map of predicate to field info class type
     */
    Map<Predicate<FieldInfo>, Class<? extends FieldInfo>> getTypes();

}
