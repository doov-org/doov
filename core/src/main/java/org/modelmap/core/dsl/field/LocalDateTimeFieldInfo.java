/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import java.time.LocalTime;

import org.modelmap.core.FieldId;

public class LocalDateTimeFieldInfo extends DefaultFieldInfo<LocalTime> {

    LocalDateTimeFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, LocalTime.class, new Class[] {}, siblings);
    }

}
