/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial.adapter;

import io.doov.core.FieldInfo;
import io.doov.core.serial.TypeAdapter;

public class LongTypeAdapter implements TypeAdapter {

    @Override
    public boolean accept(FieldInfo info) {
        return Long.class.equals(info.type()) || Long.TYPE.equals(info.type());
    }

    @Override
    public boolean accept(Object value) {
        return value instanceof Long;
    }

    @Override
    public String toString(Object value) {
        return String.valueOf(value);
    }

    @Override
    public Object fromString(FieldInfo info, String value) {
        return Long.parseLong(value);
    }
}
