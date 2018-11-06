/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.num;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public interface LongOperators extends NumericOperators<Long> {
    @Override
    default BiFunction<Long, Long, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    default BiFunction<Long, Long, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    default BiFunction<Long, Long, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    default BiFunction<Long, Long, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

    @Override
    default BinaryOperator<Long> minFunction() {
        return Long::min;
    }

    @Override
    default BinaryOperator<Long> sumFunction() {
        return Long::sum;
    }

    @Override
    default BiFunction<Long, Integer, Long> timesFunction() {
        return (l, r) -> l * r;
    }

    @Override
    default Long identity() {
        return 0L;
    }
}
