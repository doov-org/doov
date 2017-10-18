/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.LocalDateTime;
import java.util.Optional;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.meta.FieldMetadata;

public class LocalDateTimeFieldInfo extends DefaultFieldInfo<LocalDateTime>
                implements TemporalFieldInfo<LocalDateTimeFieldInfo, LocalDateTime> {

    LocalDateTimeFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, LocalDateTime.class, new Class[] {}, siblings);
    }

    @Override
    public TemporalCondition<LocalDateTimeFieldInfo, LocalDateTime> getTemporalCondition() {
        return new LocalDateTimeCondition(this);
    }

    @Override
    public NumericCondition<LongFieldInfo, Long> getAgeAtStepCondition(LocalDateTime value) {
        return new LongCondition(FieldMetadata.ageAtMetadata(this, value),
                        model -> Optional.ofNullable(model.<LocalDateTime> get(this.id()))
                                        .map(d -> YEARS.between(d, value)));
    }

}
