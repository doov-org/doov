package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.*;

import java.util.Optional;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.DefaultFieldInfo;
import io.doov.core.dsl.meta.FieldMetadata;
import io.doov.core.dsl.meta.Metadata;

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

    public final NumericStepCondition<N> lesserThan(N value) {
        return condition(lesserThanMetadata(field, value), lesserThanFunction(value));
    }

    public final NumericStepCondition<N> lesserOrEquals(N value) {
        return condition(lesserOrEqualsMetadata(field, value), lesserOrEqualsFunction(value));
    }

    public final NumericStepCondition<N> greaterThan(N value) {
        return condition(greaterThanMetadata(field, value), greaterThanFunction(value));
    }

    public final NumericStepCondition<N> greaterOrEquals(N value) {
        return condition(greaterOrEqualsMetadata(field, value), greaterOrEqualsFunction(value));
    }

    public final NumericStepCondition<N> between(N minIncluded, N maxExcluded) {
        return condition(betweenMetadata(field, minIncluded, maxExcluded), betweenFunction(minIncluded, maxExcluded));
    }

    public final NumericStepCondition<N> lesserThan(F field) {
        return conditionField(lesserThanMetadata(this.field, field),
                        model -> lesserThanFunction(model.<N> get(field.id())));
    }

    public final NumericStepCondition<N> lesserOrEquals(F field) {
        return conditionField(lesserOrEqualsMetadata(this.field, field),
                        model -> lesserOrEqualsFunction(model.<N> get(field.id())));
    }

    public final NumericStepCondition<N> greaterThan(F field) {
        return conditionField(greaterThanMetadata(this.field, field),
                        model -> greaterThanFunction(model.<N> get(field.id())));
    }

    public final NumericStepCondition<N> greaterOrEquals(F field) {
        return conditionField(greaterOrEqualsMetadata(this.field, field),
                        model -> greaterOrEqualsFunction(model.<N> get(field.id())));
    }

    public final NumericStepCondition<N> between(F minIncluded, F maxExcluded) {
        return conditionField(greaterOrEqualsMetadata(this.field, field),
                        model -> betweenFunction(model.<N> get(minIncluded.id()), model.<N> get(maxExcluded.id())));
    }

    public abstract Function<N, Boolean> lesserThanFunction(N value);

    public abstract Function<N, Boolean> lesserOrEqualsFunction(N value);

    public abstract Function<N, Boolean> greaterThanFunction(N value);

    public abstract Function<N, Boolean> greaterOrEqualsFunction(N value);

    public abstract Function<N, Boolean> betweenFunction(N minIncluded, N maxExcluded);

    private NumericStepCondition<N> condition(FieldMetadata metadata, Function<N, Boolean> predicate) {
        return conditionField(metadata, model -> predicate);
    }

    private NumericStepCondition<N> conditionField(FieldMetadata metadata,
                    Function<FieldModel, Function<N, Boolean>> predicate) {
        return new NumericStepCondition<>(this.metadata.merge(metadata), this.value, predicate);
    }

    private static class NumericStepCondition<N extends Number> extends AbstractStepCondition {

        NumericStepCondition(Metadata metadata,
                        Function<FieldModel, Optional<N>> value,
                        Function<FieldModel, Function<N, Boolean>> predicate) {
            super(metadata, model -> value.apply(model).map(predicate.apply(model)).orElse(false));
        }

    }

}
