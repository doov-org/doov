package org.modelmap.core.dsl.field;

import java.util.Optional;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.DefaultStepCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class IntegerFieldInfo extends DefaultFieldInfo<Integer> implements NumericFieldInfo<Integer> {

    IntegerFieldInfo(FieldId fieldId, Class<?> type, FieldId[] siblings) {
        super(fieldId, type, new Class[] {}, siblings);
    }

    @Override
    public StepCondition lessThan(Integer value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Integer> get(id())) //
                        .map(v -> v < value).orElse(false));
    }

    @Override
    public StepCondition lessOrEqual(Integer value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Integer> get(id())) //
                        .map(v -> v <= value).orElse(false));
    }

    @Override
    public StepCondition greaterThan(Integer value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Integer> get(id())) //
                        .map(v -> v > value).orElse(false));
    }

    @Override
    public StepCondition greaterOrEqual(Integer value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Integer> get(id())) //
                        .map(v -> v >= value).orElse(false));
    }
}
