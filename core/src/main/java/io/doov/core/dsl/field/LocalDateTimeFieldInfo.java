/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import java.time.LocalDateTime;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.LocalDateTimeCondition;
import io.doov.core.dsl.impl.TemporalCondition;

public class LocalDateTimeFieldInfo extends DefaultFieldInfo<LocalDateTime>
                implements TemporalFieldInfo<LocalDateTimeFieldInfo, LocalDateTime> {

    LocalDateTimeFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, LocalDateTime.class, new Class[] {}, siblings);
    }

    @Override
    public TemporalCondition<LocalDateTimeFieldInfo, LocalDateTime> getTemporalCondition() {
        return new LocalDateTimeCondition(this);
    }

}
