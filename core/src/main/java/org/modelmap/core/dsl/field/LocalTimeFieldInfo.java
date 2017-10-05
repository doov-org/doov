/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import java.time.LocalDateTime;

import org.modelmap.core.FieldId;

public class LocalTimeFieldInfo extends DefaultFieldInfo<LocalDateTime> {

    LocalTimeFieldInfo(FieldId fieldId, FieldId[] siblings) {
        super(fieldId, LocalDateTime.class, new Class[] {}, siblings);
    }

}
