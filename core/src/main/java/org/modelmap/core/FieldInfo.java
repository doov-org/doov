package org.modelmap.core;

/**
 * Properties of a {@code FieldId}, generated from the model java bean
 */
public interface FieldInfo {

    /**
     * @return the referenced {@code FieldId}
     */
    FieldId id();

    /**
     * @return the other {@code FieldId} mapped on the same property
     */
    FieldId[] siblings();

    /**
     * @return the {@code FieldId} type
     */
    Class<?> type();
}
