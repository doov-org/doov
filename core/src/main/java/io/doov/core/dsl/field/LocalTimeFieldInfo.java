/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import java.time.LocalDateTime;

import io.doov.core.FieldId;

public class LocalTimeFieldInfo extends DefaultFieldInfo<LocalDateTime> {

    LocalTimeFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, LocalDateTime.class, new Class[] {}, siblings);
    }

}
