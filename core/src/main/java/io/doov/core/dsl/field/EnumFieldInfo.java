/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;

public class EnumFieldInfo<E extends Enum<E>> extends DefaultFieldInfo<E> {

    EnumFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

}
