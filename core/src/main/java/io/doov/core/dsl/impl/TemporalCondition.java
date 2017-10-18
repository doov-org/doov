package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.afterMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.beforeMetadata;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.DefaultFieldInfo;
import io.doov.core.dsl.field.LongFieldInfo;
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

    // before

    public final StepCondition before(N value) {
        return step(beforeMetadata(field, value), model -> beforeFunction(value));
    }

    public final StepCondition before(F value) {
        return step(beforeMetadata(this.field, value), model -> beforeFunction(value(model, value)));
    }

    public final StepCondition before(Supplier<N> value) {
        return step(beforeMetadata(this.field, value), model -> beforeFunction(value.get()));
    }

    public final StepCondition beforeOrEq(N value) {
        return LogicalBinaryCondition.or(before(value), TypeCondition.eq(field, value));
    }

    // after

    public final StepCondition after(N value) {
        return step(afterMetadata(field, value), model -> afterFunction(value));
    }

    public final StepCondition after(F value) {
        return step(afterMetadata(this.field, value), model -> afterFunction(value(model, value)));
    }

    public final StepCondition after(Supplier<N> value) {
        return step(afterMetadata(this.field, value), model -> afterFunction(value.get()));
    }

    public final StepCondition afterOrEq(N value) {
        return LogicalBinaryCondition.or(after(value), TypeCondition.eq(field, value));
    }

    // between

    public final StepCondition between(N minValue, N maxValue) {
        return LogicalBinaryCondition.and(beforeOrEq(maxValue), afterOrEq(minValue));
    }

    public final StepCondition notBetween(N minValue, N maxValue) {
        return LogicalUnaryCondition.negate(between(minValue, maxValue));
    }

    // age

    public final NumericCondition<LongFieldInfo, Long> ageAt(N value) {
        return new LongCondition(FieldMetadata.ageAtMetadata(field, value),
                        model -> Optional.of(value(model, field))
                                        .map(betweenFunction(YEARS, value)));
    }

    public final NumericCondition<LongFieldInfo, Long> ageAt(F value) {
        return new LongCondition(FieldMetadata.ageAtMetadata(this.field, value),
                        model -> Optional.of(value(model, this.field))
                                        .map(betweenFunction(YEARS, value(model, value))));
    }

    public final NumericCondition<LongFieldInfo, Long> ageAt(Supplier<N> value) {
        return new LongCondition(FieldMetadata.ageAtMetadata(this.field, value),
                        model -> Optional.of(value(model, field))
                                        .map(betweenFunction(YEARS, value.get())));
    }

    abstract Function<N, Boolean> afterFunction(N value);

    abstract Function<N, Boolean> beforeFunction(N value);

    abstract Function<N, Long> betweenFunction(ChronoUnit unit, N value);

}
