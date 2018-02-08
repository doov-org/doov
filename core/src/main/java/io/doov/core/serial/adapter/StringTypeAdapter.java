/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial.adapter;

import io.doov.core.FieldInfo;
import io.doov.core.serial.TypeAdapter;

public class StringTypeAdapter implements TypeAdapter {

    @Override
    public boolean accept(FieldInfo info) {
        return String.class.equals(info.type());
    }

    @Override
    public boolean accept(Object value) {
        return value instanceof String;
    }

    @Override
    public String toString(Object value) {
        return value.toString();
    }

    @Override
    public Object fromString(FieldInfo info, String value) {
        return value;
    }
}
