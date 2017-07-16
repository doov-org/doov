package org.modelmap.core.dsl.field;

import java.util.Optional;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.DefaultStepCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class LongFieldInfo extends DefaultFieldInfo<Long> implements NumericFieldInfo<Long> {

    LongFieldInfo(FieldId fieldId, Class<?> type, FieldId[] siblings) {
        super(fieldId, type, new Class[] {}, siblings);
    }

    @Override
    public StepCondition lessThan(Long value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Long> get(id())) //
                        .map(v -> v < value).orElse(false));
    }

    @Override
    public StepCondition lessOrEqual(Long value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Long> get(id())) //
                        .map(v -> v <= value).orElse(false));
    }

    @Override
    public StepCondition greaterThan(Long value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Long> get(id())) //
                        .map(v -> v > value).orElse(false));
    }

    @Override
    public StepCondition greaterOrEqual(Long value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Long> get(id())) //
                        .map(v -> v >= value).orElse(false));
    }
}
