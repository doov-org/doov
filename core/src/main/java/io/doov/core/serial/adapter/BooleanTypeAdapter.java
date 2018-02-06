/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial.adapter;

import io.doov.core.FieldInfo;
import io.doov.core.serial.TypeAdapter;

public class BooleanTypeAdapter implements TypeAdapter {

    @Override
    public boolean accept(FieldInfo info) {
        return Boolean.class.equals(info.type()) || Boolean.TYPE.equals(info.type());
    }

    @Override
    public boolean accept(Object value) {
        return value instanceof Boolean;
    }

    @Override
    public String toString(Object value) {
        return String.valueOf(value);
    }

    @Override
    public Object fromString(FieldInfo fieldInfo, String value) {
        return Boolean.parseBoolean(value);
    }
}