package io.doov.core.dsl.field;

import java.time.temporal.Temporal;
import java.util.function.Supplier;

import io.doov.core.dsl.impl.TemporalCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface TemporalFieldInfo<F extends DefaultFieldInfo<N>, N extends Temporal> {

    default StepCondition before(N value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition after(N value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition before(F field) {
        return getTemporalCondition().before(field);
    }

    default StepCondition after(F field) {
        return getTemporalCondition().after(field);
    }

    default StepCondition before(Supplier<N> supplier) {
        return getTemporalCondition().before(supplier);
    }

    default StepCondition after(Supplier<N> supplier) {
        return getTemporalCondition().after(supplier);
    }

    default StepCondition beforeOrEq(N value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    default StepCondition afterOrEq(N value) {
        return getTemporalCondition().afterOrEq(value);
    }

    default StepCondition between(N minValue, N maxValue) {
        return getTemporalCondition().between(minValue, maxValue);
    }

    default StepCondition notBetween(N minValue, N maxValue) {
        return getTemporalCondition().notBetween(minValue, maxValue);
    }

    TemporalCondition<F, N> getTemporalCondition();

}
