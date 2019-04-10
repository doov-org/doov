/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.grammar.mapping.FunctionOut;
import io.doov.core.dsl.grammar.Value;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;

public class ConsumerOutput<T> extends AbstractDSLBuilder<T> implements MappingOutput<T> {

    private final TriConsumer<FieldModel, Context, T> outputFunction;
    private final MappingMetadata metadata;

    public ConsumerOutput(MappingMetadata metadata, TriConsumer<FieldModel, Context, T> outputFunction) {
        this.metadata = metadata;
        this.outputFunction = outputFunction;
    }

    public ConsumerOutput(TriConsumer<FieldModel, Context, T> outputFunction) {
        this(MappingMetadata.functionOutput(), outputFunction);
    }

    @Override
    public boolean validate(FieldModel outModel) {
        return true;
    }

    @Override
    public Value<T> ast() {
        return new FunctionOut<>();
    }

    @Override
    public MappingMetadata metadata() {
        return metadata;
    }

    @Override
    public void write(FieldModel outModel, Context context, T value) {
        outputFunction.accept(outModel, context, value);
    }
}
