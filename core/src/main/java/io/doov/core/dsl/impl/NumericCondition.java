package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.greaterOrEqualsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.greaterThanMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.lesserOrEqualsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.lesserThanMetadata;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.DefaultFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public abstract class NumericCondition<F extends DefaultFieldInfo<N>, N extends Number>
                extends DefaultCondition<F, N> {

    NumericCondition(F field) {
        super(field);
    }

    NumericCondition(FieldMetadata metadata, Function<FieldModel, Optional<N>> value) {
        super(metadata, value);
    }

    // lesser than

    public final StepCondition lesserThan(N value) {
        return step(lesserThanMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        (l, r) -> lesserThanFunction().apply(l, r));
    }

    public final StepCondition lesserThan(F value) {
        return step(lesserThanMetadata(this.field, value),
                        model -> value(model, value),
                        (l, r) -> lesserThanFunction().apply(l, r));
    }

    public final StepCondition lesserOrEquals(N value) {
        return step(lesserOrEqualsMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    public final StepCondition lesserOrEquals(F value) {
        return step(lesserOrEqualsMetadata(this.field, value),
                        model -> value(model, value),
                        (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    public abstract BiFunction<N, N, Boolean> lesserThanFunction();

    public abstract BiFunction<N, N, Boolean> lesserOrEqualsFunction();

    // greater than

    public final StepCondition greaterThan(N value) {
        return step(greaterThanMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        (l, r) -> greaterThanFunction().apply(l, r));
    }

    public final StepCondition greaterThan(F value) {
        return step(greaterThanMetadata(this.field, value),
                        model -> value(model, value),
                        (l, r) -> greaterThanFunction().apply(l, r));
    }

    public final StepCondition greaterOrEquals(N value) {
        return step(greaterOrEqualsMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    public final StepCondition greaterOrEquals(F value) {
        return step(greaterOrEqualsMetadata(this.field, value),
                        model -> value(model, value),
                        (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    public abstract BiFunction<N, N, Boolean> greaterThanFunction();

    public abstract BiFunction<N, N, Boolean> greaterOrEqualsFunction();

    // between

    public final StepCondition between(N minIncluded, N maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    public final StepCondition between(F minIncluded, F maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

}
