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

public class TypeContextConverters {

    // Simple Converters

    public static <I, O> TypeConverter<I, O> converter(BiFunction<Context, Optional<I>, O> converter,
            String description) {
        return new DefaultTypeConverter<>(converter, description);
    }

    // BiConverters

    public static <I, J, O> BiTypeConverter<I, J, O> biConverter(
            TriFunction<Context, Optional<I>, Optional<J>, O> converter, String description) {
        return new DefaultBiTypeConverter<>(converter, description);
    }

    // NaryConverters

    public static <O> NaryTypeConverter<O> nConverter(TriFunction<DslModel, Context, List<DslField>, O> function,
            String description) {
        return new DefaultNaryTypeConverter<>(function, description);
    }

}
