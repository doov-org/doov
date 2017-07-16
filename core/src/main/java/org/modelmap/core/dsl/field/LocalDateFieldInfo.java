package org.modelmap.core.dsl.field;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.DefaultStepCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class LocalDateFieldInfo extends DefaultFieldInfo<LocalDate> implements TemporalFieldInfo<LocalDate> {

    LocalDateFieldInfo(FieldId fieldId, FieldId[] siblings) {
        super(fieldId, LocalDate.class, new Class[] {}, siblings);
    }

    @Override
    public StepCondition isBefore(LocalDate value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<LocalDate> get(id())) //
                        .map(v -> v.isBefore(value)).orElse(false));
    }

    @Override
    public StepCondition isAfter(LocalDate value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<LocalDate> get(id())) //
                        .map(v -> v.isAfter(value)).orElse(false));
    }
}
