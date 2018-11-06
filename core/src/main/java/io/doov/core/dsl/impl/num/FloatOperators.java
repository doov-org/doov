/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.num;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public interface FloatOperators extends NumericOperators<Float> {

    @Override
    default BiFunction<Float, Float, Boolean> lesserThanFunction() {
        return (l, r) -> l < r;
    }

    @Override
    default BiFunction<Float, Float, Boolean> lesserOrEqualsFunction() {
        return (l, r) -> l <= r;
    }

    @Override
    default BiFunction<Float, Float, Boolean> greaterThanFunction() {
        return (l, r) -> l > r;
    }

    @Override
    default BiFunction<Float, Float, Boolean> greaterOrEqualsFunction() {
        return (l, r) -> l >= r;
    }

    @Override
    default BinaryOperator<Float> minFunction() {
        return Float::min;
    }

    @Override
    default BinaryOperator<Float> sumFunction() {
        return Float::sum;
    }

    @Override
    default BiFunction<Float, Integer, Float> timesFunction() {
        return (l, r) -> l * r;
    }

    @Override
    default Float identity() {
        return 0f;
    }
}
