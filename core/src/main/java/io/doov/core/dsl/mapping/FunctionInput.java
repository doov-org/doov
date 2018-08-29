/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.MappingInput;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class FunctionInput<T> implements MappingInput<T> {

    private final BiFunction<DslModel, Context, T> valueFunction;
    private final MappingMetadata metadata;

    public FunctionInput(MappingMetadata metadata, BiFunction<DslModel, Context, T> valueFunction) {
        this.metadata = metadata;
        this.valueFunction = valueFunction;
    }

    public FunctionInput(BiFunction<DslModel, Context, T> valueFunction) {
        this(MappingMetadata.functionInput(), valueFunction);
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return true;
    }

    @Override
    public MappingMetadata getMetadata() {
        return metadata;
    }

    @Override
    public T read(DslModel inModel, Context context) {
        return valueFunction.apply(inModel, context);
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
    }
}
