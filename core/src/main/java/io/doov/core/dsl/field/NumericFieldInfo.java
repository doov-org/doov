package io.doov.core.dsl.field;

import io.doov.core.dsl.impl.NumericCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface NumericFieldInfo<F extends DefaultFieldInfo<N>, N extends Number> {

    // lesser than

    default StepCondition lesserThan(N value) {
        return getNumericCondition().lesserThan(value);
    }

    default StepCondition lesserOrEquals(N value) {
        return getNumericCondition().lesserOrEquals(value);
    }

    default StepCondition lesserThan(F field) {
        return getNumericCondition().lesserThan(field);
    }

    default StepCondition lesserOrEquals(F field) {
        return getNumericCondition().lesserOrEquals(field);
    }

    // greater than

    default StepCondition greaterThan(N value) {
        return getNumericCondition().greaterThan(value);
    }

    default StepCondition greaterThan(F field) {
        return getNumericCondition().greaterThan(field);
    }

    default StepCondition greaterOrEquals(N value) {
        return getNumericCondition().greaterOrEquals(value);
    }

    default StepCondition greaterOrEquals(F field) {
        return getNumericCondition().greaterOrEquals(field);
    }

    // between

    default StepCondition between(N minIncluded, N maxExcluded) {
        return getNumericCondition().between(minIncluded, maxExcluded);
    }

    default StepCondition between(F minIncluded, F maxExcluded) {
        return getNumericCondition().between(minIncluded, maxExcluded);
    }

    // abstract

    NumericCondition<F, N> getNumericCondition();

}
