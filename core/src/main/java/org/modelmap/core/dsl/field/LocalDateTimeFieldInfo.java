package org.modelmap.core.dsl.field;

import java.time.LocalTime;
import java.util.Optional;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.DefaultStepCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class LocalDateTimeFieldInfo extends DefaultFieldInfo<LocalTime> implements TemporalFieldInfo<LocalTime> {

    LocalDateTimeFieldInfo(FieldId fieldId, FieldId[] siblings) {
        super(fieldId, LocalTime.class, new Class[] {}, siblings);
    }

    @Override
    public StepCondition isBefore(LocalTime value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<LocalTime> get(id())) //
                        .map(v -> v.isBefore(value)).orElse(false));
    }

    @Override
    public StepCondition isAfter(LocalTime value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<LocalTime> get(id())) //
                        .map(v -> v.isAfter(value)).orElse(false));
    }
}
