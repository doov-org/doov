/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class ConverterInput<S, T> extends AbstractDSLBuilder implements MappingInput<T> {

    private final MappingInput<S> sourceInput;
    private final TypeConverter<S, T> typeConverter;
    private final MappingInputMetadata metadata;

    public ConverterInput(MappingInput<S> sourceInput, TypeConverter<S, T> typeConverter) {
        this.sourceInput = sourceInput;
        this.typeConverter = typeConverter;
        this.metadata = MappingInputMetadata.inputMetadata(sourceInput.metadata(), typeConverter.metadata());
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return sourceInput.validate(inModel);
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public T read(DslModel inModel, Context context) {
        return typeConverter.convert(inModel, context, sourceInput.read(inModel, context));
    }

}
