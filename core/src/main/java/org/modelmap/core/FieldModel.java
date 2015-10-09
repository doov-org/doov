package org.modelmap.core;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.stream.Stream;

/**
 * An model that maps {@code FieldId} to values. Each {@code FieldId} can map to at most one value.
 */
public interface FieldModel extends Iterable<Map.Entry<FieldId, Object>> {

    /**
     * @param fieldId the {@code FieldId} to read
     * @return the the {@code FieldId} value
     */
    <T> T get(FieldId fieldId);

    /**
     * @param fieldId the {@code FieldId} to update
     * @param value the new {@code FieldId} value
     */
    <T> void set(FieldId fieldId, T value);

    /**
     * return a sequential {@code Stream} with all key-value pairs
     */
    Stream<Map.Entry<FieldId, Object>> stream();

    @Override
    Spliterator<Map.Entry<FieldId, Object>> spliterator();

    /**
     * return a parallel {@code Stream} with all key-value pairs
     */
    Stream<Map.Entry<FieldId, Object>> parallelStream();

    /**
     * @return the {@code FieldInfo} FieldInfo for all this model {@code FieldId}
     */
    FieldInfo[] getFieldInfos();

    /**
     * @return all {@code FieldId}, with a not-null value
     */
    default List<FieldId> getFieldIds() {
        return Arrays.stream(getFieldInfos()).map(FieldInfo::id).collect(toList());
    }

    /**
     * Copy all the values for the {@code FieldModel} <code>source</code>
     *
     * @param source the source field model
     */
    default void setAll(FieldModel source) {
        Arrays.stream(getFieldInfos()).filter(info -> source.get(info.id()) != null).forEach(
                        info -> set(info.id(), source.get(info.id())));
    }

    /**
     * For all the {@code FieldId}, set their value to <code>null</code>
     */
    default void clear() {
        Arrays.stream(getFieldInfos()).filter(info -> get(info.id()) != null).forEach(info -> set(info.id(), null));
    }

    /**
     * For all the {@code FieldId} tagged with the specified {@code TagId}, set their value to <code>null</code>
     */
    default void clear(TagId tag) {
        Arrays.stream(getFieldInfos()).filter(info -> asList(info.id().tags()).contains(tag) && get(info.id()) != null)
                        .forEach(info -> set(info.id(), null));
    }

}
