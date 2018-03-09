/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial;

import io.doov.core.FieldInfo;

/**
 *
 */
public interface TypeAdapter {

    /**
     * Called before {@link #fromString(FieldInfo, String)}
     *
     * @param info field info
     * @return true if this type adapter accepts to deserialize this field
     */
    boolean accept(FieldInfo info);

    /**
     * Called before {@link #toString(Object)}
     *
     * @param value
     * @return true if this type adapter accepts to serialize this object
     */
    boolean accept(Object value);

    /**
     * Serialize
     *
     * @param value object
     * @return string representation of the object
     */
    String toString(Object value);

    /**
     * Deserialize
     *
     * @param info  field info
     * @param value string value
     * @return object value
     */
    Object fromString(FieldInfo info, String value);
}
