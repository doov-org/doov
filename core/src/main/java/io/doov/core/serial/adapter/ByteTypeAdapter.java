/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial.adapter;

import io.doov.core.FieldInfo;
import io.doov.core.serial.TypeAdapter;

public class ByteTypeAdapter implements TypeAdapter {

    @Override
    public boolean accept(FieldInfo info) {
        return Byte.class.equals(info.type()) || Byte.TYPE.equals(info.type());
    }

    @Override
    public boolean accept(Object value) {
        return value instanceof Byte;
    }

    @Override
    public String toString(Object value) {
        return ((Byte) value).toString();
    }

    @Override
    public Object fromString(FieldInfo info, String value) {
        return Byte.valueOf(value);
    }
}
