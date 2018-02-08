/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial.adapter;

import io.doov.core.*;
import io.doov.core.serial.TypeAdapter;

public class CodeValuableEnumTypeAdapter implements TypeAdapter {

    @Override
    public boolean accept(FieldInfo info) {
        return info.isCodeLookup() || info.isCodeValuable();
    }

    @Override
    public boolean accept(Object value) {
        return value instanceof CodeValuable;
    }

    @Override
    public String toString(Object value) {
        return ((CodeValuable) value).getCode();
    }

    @Override
    public Object fromString(FieldInfo info, String value) {
        if (info.isCodeLookup()) {
            final CodeLookup<?> codeLookup = (CodeLookup<?>) info.type().getEnumConstants()[0];
            return codeLookup.parseCode(value);
        } // else isCodeValuable
        @SuppressWarnings("unchecked")
        final Class<? extends CodeValuable> codeValuableType = (Class<? extends CodeValuable>) info.type();
        return CodeValuable.Helper.parseCode(codeValuableType, value, null);
    }

}
