/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.grammar.Value;
import io.doov.core.dsl.grammar.mapping.FunctionIn;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;

public class FunctionInput<T> extends AbstractDSLBuilder<T> implements MappingInput<T> {

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
    public boolean validate(FieldModel inModel) {
        return true;
    }

    @Override
    public MappingMetadata metadata() {
        return metadata;
    }

    @Override
    public T read(FieldModel inModel, Context context) {
        return valueFunction.apply(inModel, context);
    }

    @Override
    public Value<T> ast(){
        return new FunctionIn<>();
    }
}
