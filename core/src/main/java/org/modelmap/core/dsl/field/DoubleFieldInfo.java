package org.modelmap.core.dsl.field;

import java.util.Optional;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.DefaultStepCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class DoubleFieldInfo extends DefaultFieldInfo<Double> implements NumericFieldInfo<Double> {

    DoubleFieldInfo(FieldId fieldId, Class<?> type, FieldId[] siblings) {
        super(fieldId, type, new Class[] {}, siblings);
    }

    @Override
    public StepCondition lessThan(Double value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Double> get(id())) //
                        .map(v -> v < value).orElse(false));
    }

    @Override
    public StepCondition lessOrEqual(Double value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Double> get(id())) //
                        .map(v -> v <= value).orElse(false));
    }

    @Override
    public StepCondition greaterThan(Double value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Double> get(id())) //
                        .map(v -> v > value).orElse(false));
    }

    @Override
    public StepCondition greaterOrEqual(Double value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Double> get(id())) //
                        .map(v -> v >= value).orElse(false));
    }
}
