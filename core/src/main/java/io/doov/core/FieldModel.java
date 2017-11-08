/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.core;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Stream;

import io.doov.core.dsl.DslId;
import io.doov.core.dsl.DslModel;

/**
 * An model that maps {@code FieldId} to values. Each {@code FieldId} can map to at most one value.
 */
public interface FieldModel extends Iterable<Map.Entry<FieldId, Object>>, DslModel {

    /**
     * @param fieldId the {@code FieldId} to read
     * @return the the {@code FieldId} value
     */
    @Override
    <T> T get(DslId fieldId);

    /**
     * @param fieldId the {@code FieldId} to update
     * @param value the new {@code FieldId} value
     */
    <T> void set(FieldId fieldId, T value);

    /**
     * return a sequential {@code Stream} with all key-value pairs
     */
    Stream<Map.Entry<FieldId, Object>> stream();

    /**
     * {@inheritDoc}
     */
    @Override
    Spliterator<Map.Entry<FieldId, Object>> spliterator();

    /**
     * return a parallel {@code Stream} with all key-value pairs
     */
    Stream<Map.Entry<FieldId, Object>> parallelStream();

    /**
     * @return the {@code FieldInfo} FieldInfo for all this model {@code FieldId}
     */
    List<FieldInfo> getFieldInfos();

    /**
     * @return all {@code FieldId}, with a not-null value
     */
    default List<FieldId> getFieldIds() {
        return getFieldInfos().stream().map(FieldInfo::id).collect(toList());
    }

    /**
     * Copy all the values for the {@code FieldModel} <code>source</code>
     *
     * @param source the source field model
     */
    default void setAll(FieldModel source) {
        getFieldInfos().stream().filter(info -> source.get(info.id()) != null)
                        .forEach(info -> set(info.id(), source.get(info.id())));
    }

    /**
     * For all the {@code FieldId}, set their value to <code>null</code>
     */
    default void clear() {
        getFieldInfos().stream().filter(info -> get(info.id()) != null).forEach(info -> set(info.id(), null));
    }

    /**
     * For all the {@code FieldId} tagged with the specified {@code TagId}, set their value to <code>null</code>
     */
    default void clear(TagId tag) {
        getFieldInfos().stream().filter(info -> info.id().hasTag(tag) && get(info.id()) != null)
                        .forEach(info -> set(info.id(), null));
    }

    default FieldInfo info(FieldId id) {
        return getFieldInfos().stream().filter(info -> info.id() == id).findFirst().orElse(null);
    }

}
