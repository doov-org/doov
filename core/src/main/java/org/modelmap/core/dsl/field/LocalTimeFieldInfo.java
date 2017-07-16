package org.modelmap.core.dsl.field;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.DefaultStepCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class LocalTimeFieldInfo extends DefaultFieldInfo<LocalDateTime> implements TemporalFieldInfo<LocalDateTime> {

    LocalTimeFieldInfo(FieldId fieldId, FieldId[] siblings) {
        super(fieldId, LocalDateTime.class, new Class[] {}, siblings);
    }

    @Override
    public StepCondition isBefore(LocalDateTime value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<LocalDateTime> get(id())) //
                        .map(v -> v.isBefore(value)).orElse(false));
    }

    @Override
    public StepCondition isAfter(LocalDateTime value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<LocalDateTime> get(id())) //
                        .map(v -> v.isAfter(value)).orElse(false));
    }
}
