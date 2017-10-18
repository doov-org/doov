package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.*;

import java.util.Optional;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.DefaultFieldInfo;
import io.doov.core.dsl.meta.FieldMetadata;

public abstract class NumericCondition<F extends DefaultFieldInfo<N>, N extends Number>
                extends DefaultCondition<F, N> {

    NumericCondition(F field) {
        super(field);
    }

    NumericCondition(FieldMetadata metadata, Function<FieldModel, Optional<N>> value) {
        super(metadata, value);
    }

    public final DefaultStepCondition<N> lesserThan(N value) {
        return step(lesserThanMetadata(field, value),
                        model -> lesserThanFunction(value));
    }

    public final DefaultStepCondition<N> lesserOrEquals(N value) {
        return step(lesserOrEqualsMetadata(field, value),
                        model -> lesserOrEqualsFunction(value));
    }

    public final DefaultStepCondition<N> greaterThan(N value) {
        return step(greaterThanMetadata(field, value),
                        model -> greaterThanFunction(value));
    }

    public final DefaultStepCondition<N> greaterOrEquals(N value) {
        return step(greaterOrEqualsMetadata(field, value),
                        model -> greaterOrEqualsFunction(value));
    }

    public final DefaultStepCondition<N> between(N minIncluded, N maxExcluded) {
        return step(betweenMetadata(field, minIncluded, maxExcluded),
                        model -> betweenFunction(minIncluded, maxExcluded));
    }

    public final DefaultStepCondition<N> lesserThan(F field) {
        return step(lesserThanMetadata(this.field, field),
                        model -> lesserThanFunction(value(model, field)));
    }

    public final DefaultStepCondition<N> lesserOrEquals(F field) {
        return step(lesserOrEqualsMetadata(this.field, field),
                        model -> lesserOrEqualsFunction(value(model, field)));
    }

    public final DefaultStepCondition<N> greaterThan(F field) {
        return step(greaterThanMetadata(this.field, field),
                        model -> greaterThanFunction(value(model, field)));
    }

    public final DefaultStepCondition<N> greaterOrEquals(F field) {
        return step(greaterOrEqualsMetadata(this.field, field),
                        model -> greaterOrEqualsFunction(value(model, field)));
    }

    public final DefaultStepCondition<N> between(F minIncluded, F maxExcluded) {
        return step(greaterOrEqualsMetadata(this.field, field),
                        model -> betweenFunction(value(model, minIncluded), value(model, maxExcluded)));
    }

    public abstract Function<N, Boolean> lesserThanFunction(N value);

    public abstract Function<N, Boolean> lesserOrEqualsFunction(N value);

    public abstract Function<N, Boolean> greaterThanFunction(N value);

    public abstract Function<N, Boolean> greaterOrEqualsFunction(N value);

    public abstract Function<N, Boolean> betweenFunction(N minIncluded, N maxExcluded);

}
