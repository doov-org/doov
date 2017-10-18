package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.afterMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.beforeMetadata;

import java.time.temporal.Temporal;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.DefaultFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public abstract class TemporalCondition<F extends DefaultFieldInfo<N>, N extends Temporal>
                extends DefaultCondition<F, N> {

    TemporalCondition(F field) {
        super(field);
    }

    TemporalCondition(FieldMetadata metadata, Function<FieldModel, Optional<N>> value) {
        super(metadata, value);
    }

    public final StepCondition before(N value) {
        return step(beforeMetadata(field, value), model -> beforeFunction(value));
    }

    public final StepCondition after(N value) {
        return step(afterMetadata(field, value), model -> afterFunction(value));
    }

    public final StepCondition before(F field) {
        return step(beforeMetadata(this.field, field), model -> beforeFunction(value(model, field)));
    }

    public final StepCondition after(F field) {
        return step(afterMetadata(this.field, field), model -> afterFunction(value(model, field)));
    }

    public final StepCondition before(Supplier<N> supplier) {
        return step(beforeMetadata(this.field, supplier), model -> beforeFunction(supplier.get()));
    }

    public final StepCondition after(Supplier<N> supplier) {
        return step(afterMetadata(this.field, supplier), model -> afterFunction(supplier.get()));
    }

    public final StepCondition beforeOrEq(N value) {
        return LogicalBinaryCondition.or(before(value), TypeCondition.eq(field, value));
    }

    public final StepCondition afterOrEq(N value) {
        return LogicalBinaryCondition.or(after(value), TypeCondition.eq(field, value));
    }

    public final StepCondition between(N minValue, N maxValue) {
        return LogicalBinaryCondition.and(beforeOrEq(maxValue), afterOrEq(minValue));
    }

    public final StepCondition notBetween(N minValue, N maxValue) {
        return LogicalUnaryCondition.negate(between(minValue, maxValue));
    }

    abstract Function<N, Boolean> afterFunction(N value);

    abstract Function<N, Boolean> beforeFunction(N value);

}
