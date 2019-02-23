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

import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.stream.Stream;

import io.doov.core.dsl.DslModel;
import io.doov.core.serial.StringMapper;
import io.doov.core.serial.TypeAdapter;

/**
 * An model that maps {@code FieldId} to values. Each {@code FieldId} can map to at most one value.
 */
public interface FieldModel extends Iterable<Map.Entry<FieldId, Object>>, DslModel, StringMapper {

    /**
     * Returns the {@code FieldId} value from the {@code FieldId} to read
     *
     * @param <T>     the type of the return
     * @param fieldId the field id to get
     * @return the field value
     */
    @Override
    <T> T get(FieldId fieldId);

    /**
     * Sets the given value to the given field id.
     *
     * @param <T>     the type of the value
     * @param fieldId the field id to set
     * @param value   the value to set
     */
    @Override
    <T> void set(FieldId fieldId, T value);

    /**
     * Returns a sequential {@code Stream} with all key-value pairs
     *
     * @return the stream
     */
    Stream<Map.Entry<FieldId, Object>> stream();

    @Override
    Spliterator<Map.Entry<FieldId, Object>> spliterator();

    /**
     * Returns a parallel {@code Stream} with all key-value pairs
     *
     * @return the parallel stream
     */
    Stream<Map.Entry<FieldId, Object>> parallelStream();

    /**
     * Returns all the {@code FieldInfo} for this model
     *
     * @return the list of field infos
     */
    List<FieldInfo> getFieldInfos();

    /**
     * Returns all {@code FieldId} with a not-null value
     *
     * @return the list of field ids
     */
    default List<FieldId> getFieldIds() {
        return getFieldInfos().stream().map(FieldInfo::id).collect(toList());
    }

    /**
     * Sets all values from the given {@code FieldModel} source
     *
     * @param source the source content
     */
    default void setAll(FieldModel source) {
        getFieldInfos().stream().filter(info -> source.get(info.id()) != null)
                        .forEach(info -> set(info.id(), source.get(info.id())));
    }

    /**
     * Clears all the {@code FieldId} by setting their value to <code>null</code>
     */
    default void clear() {
        getFieldInfos().stream().filter(info -> get(info.id()) != null).forEach(info -> set(info.id(), null));
    }

    /**
     * Clears all the {@code FieldId} tagged with the specified {@code TagId} by setting their value to
     * <code>null</code>
     *
     * @param tag the tag id
     */
    default void clear(TagId tag) {
        getFieldInfos().stream().filter(info -> info.id().hasTag(tag) && get(info.id()) != null)
                        .forEach(info -> set(info.id(), null));
    }

    /**
     * Returns the field info for the given field id.
     *
     * @param id the field id
     * @return the field info
     */
    default FieldInfo info(FieldId id) {
        return getFieldInfos().stream().filter(info -> info.id() == id).findFirst().orElse(null);
    }


    @Override
    default String getAsString(FieldId fieldId) {
        Object value = get(fieldId);
        if (value == null) {
            return null;
        }
        return getTypeAdapterRegistry().stream()
                        .filter(a -> a.accept(value))
                        .findFirst()
                        .map(a -> a.toString(value))
                        .orElse(null);
    }

    @Override
    default String getAsString(FieldInfo info) {
        Objects.requireNonNull(info);
        return getAsString(info.id());
    }

    @Override
    default void setAsString(FieldId fieldId, String value) {
        FieldInfo fieldInfo = info(fieldId);
        setAsString(fieldInfo, value);
    }

    @Override
    default void setAsString(FieldInfo fieldInfo, String value) {
        Objects.requireNonNull(fieldInfo);
        if (value == null) {
            set(fieldInfo.id(), null);
        } else {
            TypeAdapter typeAdapter = getTypeAdapterRegistry().stream()
                    .filter(a -> a.accept(fieldInfo))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("cannot set field "
                            + fieldInfo.id() + " with value " + value));
            set(fieldInfo.id(), typeAdapter.fromString(fieldInfo, value));
        }
    }

}
