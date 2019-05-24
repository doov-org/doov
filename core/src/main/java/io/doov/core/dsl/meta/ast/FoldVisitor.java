/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import java.util.function.BiFunction;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class FoldVisitor<T> implements MetadataVisitor {

    private final BiFunction<T, Metadata, T> accumulator;

    private T result;

    FoldVisitor(BiFunction<T, Metadata, T> accumulator, T init) {
        this.accumulator = accumulator;
        result = init;
    }

    public T getResult() {
        return result;
    }

    @Override
    public void start(Metadata metadata, int depth) {
        result = accumulator.apply(result, metadata);
    }

}
