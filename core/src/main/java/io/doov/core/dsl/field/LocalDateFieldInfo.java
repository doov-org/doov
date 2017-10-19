/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import java.time.LocalDate;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.LocalDateCondition;
import io.doov.core.dsl.impl.TemporalCondition;

public class LocalDateFieldInfo extends DefaultFieldInfo<LocalDate>
                implements TemporalFieldInfo<LocalDateFieldInfo, LocalDate> {

    LocalDateFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, LocalDate.class, new Class[] {}, siblings);
    }

    @Override
    public TemporalCondition<LocalDateFieldInfo, LocalDate> getTemporalCondition() {
        return new LocalDateCondition(this);
    }

}
