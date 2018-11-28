/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.num;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public interface NumericOperators<N extends Number> {

    BiFunction<N, N, Boolean> lesserThanFunction();

    BiFunction<N, N, Boolean> lesserOrEqualsFunction();

    BiFunction<N, N, Boolean> greaterThanFunction();

    BiFunction<N, N, Boolean> greaterOrEqualsFunction();

    BinaryOperator<N> minFunction();

    BinaryOperator<N> sumFunction();

    BiFunction<N, Integer, N> timesFunction();

    N identity();
}
