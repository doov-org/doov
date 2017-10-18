/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import java.time.LocalTime;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.LocalTimeCondition;
import io.doov.core.dsl.impl.TemporalCondition;

public class LocalTimeFieldInfo extends DefaultFieldInfo<LocalTime>
                implements TemporalFieldInfo<LocalTimeFieldInfo, LocalTime> {

    LocalTimeFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, LocalTime.class, new Class[] {}, siblings);
    }

    @Override
    public TemporalCondition<LocalTimeFieldInfo, LocalTime> getTemporalCondition() {
        return new LocalTimeCondition(this);
    }

}
