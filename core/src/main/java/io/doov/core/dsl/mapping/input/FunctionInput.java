/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.Single;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;

public class FunctionInput<T> implements ContextAccessor<T> {

    private final BiFunction<FieldModel, Context, T> valueFunction;
    private final MappingMetadata metadata;

    public FunctionInput(MappingMetadata metadata, BiFunction<FieldModel, Context, T> valueFunction) {
        this.metadata = metadata;
        this.valueFunction = valueFunction;
    }

    public FunctionInput(BiFunction<FieldModel, Context, T> valueFunction) {
        this(MappingMetadata.functionInput(), valueFunction);
    }

    @Override
    public MappingMetadata metadata() {
        return metadata;
    }

    @Override
    public Single<T> value(FieldModel model, Context context) {
        return Single.supplied(() -> valueFunction.apply(model, context));
    }
}
