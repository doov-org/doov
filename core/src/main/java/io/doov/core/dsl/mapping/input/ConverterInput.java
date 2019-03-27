/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.Single;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingInputMetadata;
import io.doov.core.dsl.meta.Metadata;

public class ConverterInput<S, T> implements ContextAccessor<T> {

    private final ContextAccessor<S> source;
    private final TypeConverter<S, T> typeConverter;
    private final MappingInputMetadata metadata;

    public ConverterInput(ContextAccessor<S> source, TypeConverter<S, T> typeConverter) {
        this.source = source;
        this.typeConverter = typeConverter;
        this.metadata = MappingInputMetadata.inputMetadata(source.metadata(), typeConverter.metadata());
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public Single<T> value(FieldModel model, Context context) {
        return source.value(model,context)
                .map(x -> typeConverter.convert(model,context,x));
    }
}
