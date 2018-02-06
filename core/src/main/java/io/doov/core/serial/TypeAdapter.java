/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial;

import io.doov.core.FieldInfo;

public interface TypeAdapter {

    boolean accept(FieldInfo info);

    boolean accept(Object value);

    String toString(Object value);

    Object fromString(FieldInfo info, String value);
}
