/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.stream.Collector;
import java.util.stream.StreamSupport;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;

public class CollectInput<I, T extends Iterable<I>> extends AbstractDSLBuilder implements MappingInput<T> {

    private final MappingInput<? extends Iterable<I>> input;
    private final Collector<I, ?, T> collector;

    public CollectInput(MappingInput<? extends Iterable<I>> input, Collector<I, ?, T> collector) {
        this.input = input;
        this.collector = collector;
    }

    @Override
    public Metadata metadata() {
        return input.metadata();
    }

    @Override
    public T read(FieldModel inModel, Context context) {
        return StreamSupport.stream(input.read(inModel, context).spliterator(), false).collect(collector);
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return true;
    }
}
