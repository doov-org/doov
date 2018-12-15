/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.converter.*;

/**
 * Factory methods of context-aware {@code TypeConverter}s.
 * Seperated from {@code TypeConverters} to avoid function signature collusion
 */
public class TypeContextConverters {

    // Simple Converters

    /**
     * 1-to-1 converter with context as additional parameter
     *
     * @param converter   converter function
     * @param description text description
     * @param <I>         input type
     * @param <O>         output type
     * @return type converter
     */
    public static <I, O> TypeConverter<I, O> converter(BiFunction<Context, Optional<I>, O> converter,
            String description) {
        return new DefaultTypeConverter<>(converter, description);
    }

    // BiConverters

    /**
     * 2-to-1 converter with context as additional parameter
     *
     * @param converter   converter function
     * @param description text description
     * @param <I>         first input type
     * @param <J>         second input type
     * @param <O>         output type
     * @return type converter
     */
    public static <I, J, O> BiTypeConverter<I, J, O> biConverter(
            TriFunction<Context, Optional<I>, Optional<J>, O> converter, String description) {
        return new DefaultBiTypeConverter<>(converter, description);
    }

    // NaryConverters

    /**
     * N-to-1 converter with context as additional parameter
     *
     * @param function    converter function
     * @param description text description
     * @param <O>         output type
     * @return type converter
     */
    public static <O> NaryTypeConverter<O> nConverter(TriFunction<DslModel, Context, List<DslField<?>>, O> function,
            String description) {
        return new DefaultNaryTypeConverter<>(function, description);
    }

}
