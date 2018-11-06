/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.num;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public interface DoubleOperators extends NumericOperators<Double> {
    @Override
    default BiFunction<Double, Double, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    default BiFunction<Double, Double, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    default BiFunction<Double, Double, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    default BiFunction<Double, Double, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

    @Override
    default BinaryOperator<Double> minFunction() {
        return Double::min;
    }

    @Override
    default BinaryOperator<Double> sumFunction() {
        return Double::sum;
    }

    @Override
    default BiFunction<Double, Integer, Double> timesFunction() {
        return (l, r) -> l * r;
    }

    @Override
    default Double identity() {
        return 0d;
    }
}
