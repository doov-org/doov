package org.modelmap.core;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Set;

public interface FieldModel {

    <T> T get(FieldId fieldId);

    void set(FieldId fieldId, Object value);

    Set<FieldInfo> getFieldInfos();

    /**
     * @return all field ids with a not-null value
     */
    default List<FieldId> getFields() {
        return getFieldInfos().stream()
                        .filter(info -> get(info.id()) != null)
                        .map(FieldInfo::id).collect(toList());
    }

    /**
     * * @return all field ids
     */
    default List<FieldId> getAllFields() {
        return getFieldInfos().stream().map(FieldInfo::id).collect(toList());
    }

    /**
     * Copy all the values for the model <code>source</code>
     *
     * @param source the source field model
     */
    default void setAll(FieldModel source) {
        getFieldInfos().stream()
                        .filter(info -> source.get(info.id()) != null)
                        .forEach(info -> set(info.id(), source.get(info.id())));
    }

    /**
     * For all the field ids, set their value to <code>null</code>
     */
    default void clear() {
        getFieldInfos().stream()
                        .filter(info -> get(info.id()) != null)
                        .forEach(info -> set(info.id(), null));
    }

    /**
     * For all the field ids tagged with <code>tag</code>, set their value to <code>null</code>
     */
    default void clear(TagId tag) {
        getFieldInfos().stream()
                        .filter(info -> info.id().tags().contains(tag) && get(info.id()) != null)
                        .forEach(info -> set(info.id(), null));
    }
}
