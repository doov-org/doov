package org.modelmap.core.dsl.impl;

import static org.modelmap.core.dsl.meta.FieldMetadata.*;

import java.util.Optional;
import java.util.function.Function;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.DefaultFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;
import org.modelmap.core.dsl.meta.Metadata;

public abstract class NumericCondition<F extends DefaultFieldInfo<N>, N extends Number> {

    private final F field;
    private final FieldMetadata metadata;
    private final Function<FieldModel, Optional<N>> value;

    NumericCondition(F field) {
        this.field = field;
        this.metadata = emptyMetadata();
        this.value = fieldModel -> Optional.ofNullable(fieldModel.<N> get(field.id()));
    }

    NumericCondition(FieldMetadata metadata, Function<FieldModel, Optional<N>> value) {
        this.field = null;
        this.metadata = metadata;
        this.value = value;
    }

    public final NumericStepCondition lesserThan(N value) {
        return getNumericStepCondition(lesserThanMetadata(field, value),
                        lesserThanFunction(value));
    }

    public final NumericStepCondition lesserThan(F field) {
        return getNumericStepConditionField(lesserThanMetadata(this.field, field),
                        fieldModel -> lesserThanFunction(fieldModel.<N> get(field.id())));
    }

    public final NumericStepCondition lesserOrEquals(N value) {
        return getNumericStepCondition(lesserOrEqualsMetadata(field, value),
                        lesserOrEqualsFunction(value));
    }

    public final NumericStepCondition lesserOrEquals(F field) {
        return getNumericStepConditionField(lesserOrEqualsMetadata(this.field, field),
                        fieldModel -> lesserOrEqualsFunction(fieldModel.<N> get(field.id())));
    }

    public final NumericStepCondition greaterThan(N value) {
        return getNumericStepCondition(greaterThanMetadata(field, value),
                        greaterThanFunction(value));
    }

    public final NumericStepCondition greaterThan(F field) {
        return getNumericStepConditionField(greaterThanMetadata(this.field, field),
                        fieldModel -> greaterThanFunction(fieldModel.<N> get(field.id())));
    }

    public final NumericStepCondition greaterOrEquals(N value) {
        return getNumericStepCondition(greaterOrEqualsMetadata(field, value),
                        greaterOrEqualsFunction(value));
    }

    public final NumericStepCondition greaterOrEquals(F field) {
        return getNumericStepConditionField(greaterOrEqualsMetadata(this.field, field),
                        fieldModel -> greaterOrEqualsFunction(fieldModel.<N> get(field.id())));
    }

    public final NumericStepCondition between(N minIncluded, N maxExcluded) {
        return getNumericStepCondition(betweenMetadata(field, minIncluded, maxExcluded),
                        betweenFunction(minIncluded, maxExcluded));
    }

    public abstract Function<N, Boolean> lesserThanFunction(N value);

    public abstract Function<N, Boolean> lesserOrEqualsFunction(N value);

    public abstract Function<N, Boolean> greaterThanFunction(N value);

    public abstract Function<N, Boolean> greaterOrEqualsFunction(N value);

    public abstract Function<N, Boolean> betweenFunction(N minIncluded, N maxExcluded);

    private NumericStepCondition getNumericStepCondition(FieldMetadata metadata, Function<N, Boolean> predicate) {
        return getNumericStepConditionField(metadata, fieldModel -> predicate);
    }

    private NumericStepCondition getNumericStepConditionField(FieldMetadata metadata,
                    Function<FieldModel, Function<N, Boolean>> predicate) {
        return new NumericStepCondition<>(this.metadata.merge(metadata), this.value, predicate);
    }

    private static class NumericStepCondition<N extends Number> extends AbstractStepCondition {

        NumericStepCondition(Metadata metadata,
                        Function<FieldModel, Optional<N>> value,
                        Function<FieldModel, Function<N, Boolean>> predicate) {
            super(metadata, fieldModel -> value.apply(fieldModel).map(predicate.apply(fieldModel)).orElse(false));
        }

    }

}
