/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import java.time.LocalDateTime;

import org.modelmap.core.FieldId;

public class LocalTimeFieldInfo extends DefaultFieldInfo<LocalDateTime> {

    LocalTimeFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, LocalDateTime.class, new Class[] {}, siblings);
    }

    @Override
    public LocalTimeFieldInfo as(String readable) {
        return new LocalTimeFieldInfo(id(), readable, siblings());
    }

}
