/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial.adapter;

import java.time.LocalDate;

import io.doov.core.FieldInfo;
import io.doov.core.serial.TypeAdapter;

public class LocalDateTypeAdapter implements TypeAdapter {

    @Override
    public boolean accept(FieldInfo info) {
        return LocalDate.class.equals(info.type());
    }

    @Override
    public boolean accept(Object value) {
        return value instanceof LocalDate;
    }

    @Override
    public String toString(Object value) {
        return value.toString();
    }

    @Override
    public Object fromString(FieldInfo info, String value) {
        return LocalDate.parse(value);
    }
}
