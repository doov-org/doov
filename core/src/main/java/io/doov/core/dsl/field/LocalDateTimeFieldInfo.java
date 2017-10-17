/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import java.time.LocalTime;

import io.doov.core.FieldId;

public class LocalDateTimeFieldInfo extends DefaultFieldInfo<LocalTime> {

    LocalDateTimeFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, LocalTime.class, new Class[] {}, siblings);
    }

}
