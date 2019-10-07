/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.ReduceMetadata;

public class ReduceInput<I, T extends Iterable<I>, O> extends AbstractDSLBuilder implements MappingInput<O> {

    private final MappingInput<T> mappingInput;
    private final TypeConverter<Stream<I>, O> converter;
    private final ReduceMetadata metadata;

    public ReduceInput(MappingInput<T> mappingInput, TypeConverter<Stream<I>, O> converter) {
        this.mappingInput = mappingInput;
        this.converter = converter;
        this.metadata = ReduceMetadata.create(mappingInput.metadata(), converter.metadata());
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public O read(FieldModel inModel, Context context) {
        Stream<I> stream = StreamSupport.stream(mappingInput.read(inModel, context).spliterator(), false);
        return converter.convert(inModel, context, stream);
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return true;
    }
}
