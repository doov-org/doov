/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.Function;

import io.doov.core.dsl.lang.StaticTypeConverter;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultStaticTypeConverter<I, O> implements StaticTypeConverter<I, O> {

    private final Function<I, O> function;
    private final String description;

    public static <I> StaticTypeConverter<I, I> identity() {
        return converter(Function.identity(), "identity");
    }

    public static <I, O> StaticTypeConverter<I, O> converter(Function<I, O> function, String description) {
        return new DefaultStaticTypeConverter<>(function, description);
    }

    public DefaultStaticTypeConverter(Function<I, O> function, String description) {
        this.function = function;
        this.description = description;
    }

    @Override
    public O convert(I input) {
        return function.apply(input);
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {

    }

    @Override
    public String readable() {
        return description;
    }
}
