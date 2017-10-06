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
     * @return the human readable {@code FieldId}
     */
    String readable();

    /**
     * @return the {@code FieldId} type
     */
    Class<?> type();

    /**
     * @return the {@code FieldId} type parameters
     */
    Class<?>[] genericTypes();

    /**
     * @return the {@code FieldId} with different readable
     */
    FieldInfo as(String readable);

}
