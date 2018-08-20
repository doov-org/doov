/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;

public class TypeConverters {

    // Simple Converters

    public static <I, O> TypeConverter<I, O> converter(Function<I, O> converter,
            String description) {
        return new DefaultTypeConverter<>(i -> i.map(converter).orElseGet(() -> converter.apply(null)), description);
    }

    public static <I, O> TypeConverter<I, O> converter(Function<I, O> converter, O nullCase,
            String description) {
        return new DefaultTypeConverter<>(i -> i.map(converter).orElse(nullCase), description);
    }

    // ValueConverters

    public static <I, O> StaticTypeConverter<I, O> valueConverter(Function<I, O> function,
            String description) {
        return new DefaultStaticTypeConverter<>(function, description);
    }

    // BiConverters

    public static <I, J, O> BiTypeConverter<I, J, O> biConverter(BiFunction<Optional<I>, Optional<J>, O> converter,
            String description) {
        return new DefaultBiTypeConverter<>(converter, description);
    }

    public static <I, J, O> BiTypeConverter<I, J, O> biConverter(BiFunction<I, J, O> converter, O nullCase,
            String description) {
        return new DefaultBiTypeConverter<>((i, j) -> (i.isPresent() && j.isPresent()) ?
                converter.apply(i.get(), j.get()) : nullCase, description);
    }

    public static <I, J, O> BiTypeConverter<I, J, O> biConverter(BiFunction<I, J, O> converter, I nullIn, J nullIn2,
            String description) {
        return new DefaultBiTypeConverter<>((i, j) -> converter.apply(i.orElse(nullIn), j.orElse(nullIn2)),
                description);
    }

    // NaryConverters

    public static <O> NaryTypeConverter<O> nConverter(BiFunction<DslModel, List<DslField>, O> function,
            String description) {
        return new DefaultNaryTypeConverter<>(function, description);
    }

    // Utility converters

    public static NaryTypeConverter<Integer> counter(String description) {
        return nConverter((model, fieldInfos) ->
                (int) fieldInfos.stream()
                        .map(f -> model.get(f.id()))
                        .filter(Objects::nonNull)
                        .count(), description);
    }

}
