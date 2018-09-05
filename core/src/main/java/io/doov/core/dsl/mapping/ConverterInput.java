/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class ConverterInput<S, T> implements MappingInput<T> {

    private final MappingInput<S> sourceInput;
    private final TypeConverter<S, T> typeConverter;

    public ConverterInput(MappingInput<S> sourceInput, TypeConverter<S, T> typeConverter) {
        this.sourceInput = sourceInput;
        this.typeConverter = typeConverter;
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return sourceInput.validate(inModel);
    }

    @Override
    public Metadata getMetadata() {
        return sourceInput.getMetadata();
    }

    @Override
    public T read(DslModel inModel, Context context) {
        return typeConverter.convert(inModel, context, sourceInput.read(inModel, context));
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        sourceInput.accept(visitor, depth);
        typeConverter.accept(visitor, depth);
    }
}
