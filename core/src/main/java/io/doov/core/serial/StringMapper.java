/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;

/**
 * Bi-directional mapping to String representation for serialization
 */
public interface StringMapper {

    /**
     * Returns the registry for TypeAdapters
     *
     * @return the type adapter registry
     */
    TypeAdapterRegistry getTypeAdapterRegistry();

    /**
     * Gets the field value as String
     *
     * @param fieldId field id
     * @return field value as string
     */
    String getAsString(FieldId fieldId);

    /**
     * Gets the field value as String
     *
     * @param fieldInfo fieldInfo
     * @return field value as string
     */
    String getAsString(FieldInfo fieldInfo);

    /**
     * Sets the field value from String
     *
     * @param fieldId field id
     * @param value   value as string
     */
    void setAsString(FieldId fieldId, String value);

    /**
     * Sets the field value from String
     *
     * @param fieldInfo field id
     * @param value     value as string
     */
    void setAsString(FieldInfo fieldInfo, String value);
}
