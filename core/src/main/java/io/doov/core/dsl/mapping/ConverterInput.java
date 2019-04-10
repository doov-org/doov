/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.grammar.Convert1;
import io.doov.core.dsl.grammar.Value;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingInputMetadata;
import io.doov.core.dsl.meta.Metadata;

public class ConverterInput<S, T> extends AbstractDSLBuilder<T> implements MappingInput<T> {

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
    public T read(FieldModel inModel, Context context) {
        return typeConverter.convert(inModel, context, sourceInput.read(inModel, context));
    }

    @Override
    public Value<T> ast() {
        return new Convert1<>(sourceInput.ast(),typeConverter);
    }

}
