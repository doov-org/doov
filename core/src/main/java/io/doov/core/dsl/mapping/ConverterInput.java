/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.Optional;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingInputMetadata;
import io.doov.core.dsl.meta.Metadata;

public class ConverterInput<S, T> extends AbstractDSLBuilder implements ContextAccessor<T> {

    private final ContextAccessor<S> sourceInput;
    private final TypeConverter<S, T> typeConverter;
    private final MappingInputMetadata metadata;

    public ConverterInput(ContextAccessor<S> sourceInput, TypeConverter<S, T> typeConverter) {
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
    public Optional<T> value(FieldModel model, Context context) {
        Optional<S> input = sourceInput.value(model, context);
        return input.flatMap(s -> Optional.ofNullable(typeConverter.convert(model, context, s)));
    }
}
