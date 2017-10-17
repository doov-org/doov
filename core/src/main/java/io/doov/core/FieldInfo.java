package io.doov.core;

import io.doov.core.dsl.meta.Readable;

/**
 * Properties of a {@code FieldId}, generated from the model java bean
 */
public interface FieldInfo extends Readable {

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

    /**
     * @return the {@code FieldId} type parameters
     */
    Class<?>[] genericTypes();

}
