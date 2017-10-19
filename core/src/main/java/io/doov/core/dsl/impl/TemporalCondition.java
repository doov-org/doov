package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.afterMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.ageAtMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.beforeMetadata;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Optional;
import java.util.function.*;

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
        return step(beforeMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        (v1, v2) -> beforeFunction().apply(v1, v2));
    }

    public final StepCondition before(F value) {
        return step(beforeMetadata(field, value),
                        model -> value(model, value),
                        (v1, v2) -> beforeFunction().apply(v1, v2));
    }

    public final StepCondition before(Supplier<N> value) {
        return step(beforeMetadata(field, value),
                        model -> Optional.ofNullable(value.get()),
                        (v1, v2) -> beforeFunction().apply(v1, v2));
    }

    public final StepCondition beforeOrEq(N value) {
        return LogicalBinaryCondition.or(before(value), new TypeCondition<>(field).eq(value));
    }

    abstract BiFunction<N, N, Boolean> beforeFunction();

    // after

    public final StepCondition after(N value) {
        return step(afterMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        (v1, v2) -> afterFunction().apply(v1, v2));
    }

    public final StepCondition after(F value) {
        return step(afterMetadata(field, value),
                        model -> value(model, value),
                        (v1, v2) -> afterFunction().apply(v1, v2));
    }

    public final StepCondition after(Supplier<N> value) {
        return step(afterMetadata(field, value),
                        model -> Optional.ofNullable(value.get()),
                        (v1, v2) -> afterFunction().apply(v1, v2));
    }

    public final StepCondition afterOrEq(N value) {
        return LogicalBinaryCondition.or(after(value), new TypeCondition<>(field).eq(value));
    }

    abstract BiFunction<N, N, Boolean> afterFunction();

    // between

    public final StepCondition between(N minValue, N maxValue) {
        return LogicalBinaryCondition.and(beforeOrEq(maxValue), afterOrEq(minValue));
    }

    public final StepCondition notBetween(N minValue, N maxValue) {
        return LogicalUnaryCondition.negate(between(minValue, maxValue));
    }

    // age

    public final NumericCondition<LongFieldInfo, Long> ageAt(N value) {
        return new LongCondition(ageAtMetadata(field, value),
                        model -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(value)
                                                        .map(r -> betweenFunction(YEARS).apply(l, r))));
    }

    public final NumericCondition<LongFieldInfo, Long> ageAt(F value) {
        return new LongCondition(ageAtMetadata(field, value),
                        model -> value(model, field)
                                        .flatMap(l -> value(model, value)
                                                        .map(r -> betweenFunction(YEARS).apply(l, r))));
    }

    public final NumericCondition<LongFieldInfo, Long> ageAt(Supplier<N> value) {
        return new LongCondition(ageAtMetadata(field, value),
                        model -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(value.get())
                                                        .map(r -> betweenFunction(YEARS).apply(l, r))));
    }

    abstract BiFunction<N, N, Long> betweenFunction(ChronoUnit unit);

}
