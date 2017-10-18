/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.LocalTime;
import java.util.Optional;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.meta.FieldMetadata;

public class LocalTimeFieldInfo extends DefaultFieldInfo<LocalTime>
                implements TemporalFieldInfo<LocalTimeFieldInfo, LocalTime> {

    LocalTimeFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, LocalTime.class, new Class[] {}, siblings);
    }

    @Override
    public TemporalCondition<LocalTimeFieldInfo, LocalTime> getTemporalCondition() {
        return new LocalTimeCondition(this);
    }

    @Override
    public NumericCondition<LongFieldInfo, Long> getAgeAtStepCondition(LocalTime value) {
        return new LongCondition(FieldMetadata.ageAtMetadata(this, value),
                        model -> Optional.ofNullable(model.<LocalTime> get(this.id()))
                                        .map(d -> YEARS.between(d, value)));
    }

}
