package io.doov.core.dsl.impl;

import java.util.function.Function;

import io.doov.core.dsl.meta.Metadata;

public class FunctionWithMetadata<T, R> {

    final Metadata metadata;
    final Function<T, R> function;

    FunctionWithMetadata(Metadata metadata, Function<T, R> function) {
        this.metadata = metadata;
        this.function = function;
    }

}