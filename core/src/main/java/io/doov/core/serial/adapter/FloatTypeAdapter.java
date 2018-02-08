/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial.adapter;

import io.doov.core.FieldInfo;
import io.doov.core.serial.TypeAdapter;

public class FloatTypeAdapter implements TypeAdapter {

    @Override
    public boolean accept(FieldInfo info) {
        return Float.class.equals(info.type()) || Float.TYPE.equals(info.type());
    }

    @Override
    public boolean accept(Object value) {
        return value instanceof Float;
    }

    @Override
    public String toString(Object value) {
        return String.valueOf(value);
    }

    @Override
    public Object fromString(FieldInfo info, String value) {
        return Float.parseFloat(value);
    }
}
