/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.num;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public interface IntegerOperators extends NumericOperators<Integer> {

    @Override
    default BiFunction<Integer, Integer, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    default BiFunction<Integer, Integer, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    default BiFunction<Integer, Integer, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    default BiFunction<Integer, Integer, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

    @Override
    default BinaryOperator<Integer> minFunction() {
        return Integer::min;
    }

    @Override
    default BinaryOperator<Integer> sumFunction() {
        return Integer::sum;
    }

    @Override
    default BiFunction<Integer, Integer, Integer> timesFunction() {
        return (l, r) -> l * r;
    }

    @Override
    default Integer identity() {
        return 0;
    }
}
