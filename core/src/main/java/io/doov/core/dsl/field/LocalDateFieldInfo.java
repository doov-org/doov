/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.LocalDate;
import java.util.Optional;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.meta.FieldMetadata;

public class LocalDateFieldInfo extends DefaultFieldInfo<LocalDate>
                implements TemporalFieldInfo<LocalDateFieldInfo, LocalDate> {

    LocalDateFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, LocalDate.class, new Class[] {}, siblings);
    }

    @Override
    public TemporalCondition<LocalDateFieldInfo, LocalDate> getTemporalCondition() {
        return new LocalDateCondition(this);
    }

    @Override
    public NumericCondition<LongFieldInfo, Long> getAgeAtStepCondition(LocalDate value) {
        return new LongCondition(FieldMetadata.ageAtMetadata(this, value),
                        model -> Optional.ofNullable(model.<LocalDate> get(this.id()))
                                        .map(d -> YEARS.between(d, value)));
    }

}
