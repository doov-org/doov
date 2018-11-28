/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.time;

import java.time.temporal.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface TemporalOperators<N extends Temporal> {
    
    Function<N, N> minusFunction(int value, TemporalUnit unit);

    Function<N, N> plusFunction(int value, TemporalUnit unit);

    Function<N, N> withFunction(TemporalAdjuster ajuster);

    BiFunction<N, N, Boolean> afterFunction();

    BiFunction<N, N, Boolean> afterOrEqualsFunction();

    BiFunction<N, N, Boolean> beforeFunction();

    BiFunction<N, N, Boolean> beforeOrEqualsFunction();

    BiFunction<N, N, Long> betweenFunction(ChronoUnit unit);
}
