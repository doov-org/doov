/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial.adapter;

import io.doov.core.FieldInfo;
import io.doov.core.serial.TypeAdapter;

public class CharacterTypeAdapter implements TypeAdapter {

    @Override
    public boolean accept(FieldInfo info) {
        return Character.class.equals(info.type()) || Character.TYPE.equals(info.type());
    }

    @Override
    public boolean accept(Object value) {
        return value instanceof Character;
    }

    @Override
    public String toString(Object value) {
        return String.valueOf(value);
    }

    @Override
    public Object fromString(FieldInfo info, String value) {
        return value.toCharArray()[0];
    }
}
