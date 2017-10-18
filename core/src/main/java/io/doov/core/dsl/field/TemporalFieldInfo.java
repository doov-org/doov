package io.doov.core.dsl.field;

import java.time.temporal.Temporal;
import java.util.function.Supplier;

import io.doov.core.dsl.impl.NumericCondition;
import io.doov.core.dsl.impl.TemporalCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface TemporalFieldInfo<F extends DefaultFieldInfo<N>, N extends Temporal> {

    // before

    default StepCondition before(N value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition before(F value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition before(Supplier<N> value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition beforeOrEq(N value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    // after

    default StepCondition after(N value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition after(F value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition after(Supplier<N> value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition afterOrEq(N value) {
        return getTemporalCondition().afterOrEq(value);
    }

    // beetween

    default StepCondition between(N minValue, N maxValue) {
        return getTemporalCondition().between(minValue, maxValue);
    }

    default StepCondition notBetween(N minValue, N maxValue) {
        return getTemporalCondition().notBetween(minValue, maxValue);
    }

    // age

    default NumericCondition<LongFieldInfo, Long> ageAt(N value) {
        return getTemporalCondition().ageAt(value);
    }

    default NumericCondition<LongFieldInfo, Long> ageAt(F value) {
        return getTemporalCondition().ageAt(value);
    }

    default NumericCondition<LongFieldInfo, Long> ageAt(Supplier<N> value) {
        return getTemporalCondition().ageAt(value);
    }

    // abstract

    TemporalCondition<F, N> getTemporalCondition();

    NumericCondition<LongFieldInfo, Long> getAgeAtStepCondition(N value);

}
