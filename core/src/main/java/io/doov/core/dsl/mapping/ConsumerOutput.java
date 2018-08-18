/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class ConsumerOutput<T> implements MappingOutput<T> {

    private final TriConsumer<DslModel, Context, T> outputFunction;
    private final MappingMetadata metadata;

    public ConsumerOutput(TriConsumer<DslModel, Context, T> outputFunction) {
        this.outputFunction = outputFunction;
        this.metadata = MappingMetadata.functionOutput();
    }

    @Override
    public boolean validate(FieldModel outModel) {
        return true;
    }

    @Override
    public MappingMetadata getMetadata() {
        return metadata;
    }

    @Override
    public void write(DslModel outModel, Context context, T value) {
        outputFunction.accept(outModel, context, value);
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
    }
}
