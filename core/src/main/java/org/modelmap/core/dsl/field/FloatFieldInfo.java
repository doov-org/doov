package org.modelmap.core.dsl.field;

import java.util.Optional;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.impl.DefaultStepCondition;
import org.modelmap.core.dsl.lang.StepCondition;

public class FloatFieldInfo extends DefaultFieldInfo<Float> implements NumericFieldInfo<Float> {

    FloatFieldInfo(FieldId fieldId, Class<?> type, FieldId[] siblings) {
        super(fieldId, type, new Class[] {}, siblings);
    }

    @Override
    public StepCondition lessThan(Float value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Float> get(id())) //
                        .map(v -> v < value).orElse(false));
    }

    @Override
    public StepCondition lessOrEqual(Float value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Float> get(id())) //
                        .map(v -> v <= value).orElse(false));
    }

    @Override
    public StepCondition greaterThan(Float value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Float> get(id())) //
                        .map(v -> v > value).orElse(false));
    }

    @Override
    public StepCondition greaterOrEqual(Float value) {
        return new DefaultStepCondition(c -> Optional.ofNullable(c.<Float> get(id())) //
                        .map(v -> v >= value).orElse(false));
    }
}
