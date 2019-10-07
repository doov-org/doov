/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.FilterMetadata;
import io.doov.core.dsl.meta.Metadata;

public class FilterInput<I> extends AbstractDSLBuilder implements MappingInput<List<I>> {

    private final MappingInput<? extends Iterable<I>> mappingInput;
    private final TypeConverter<I, Boolean> converter;
    private final FilterMetadata metadata;

    public FilterInput(MappingInput<? extends Iterable<I>> mappingInput, TypeConverter<I, Boolean> converter) {
        this.mappingInput = mappingInput;
        this.converter = converter;
        this.metadata = FilterMetadata.create(mappingInput.metadata(), converter.metadata());
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public List<I> read(FieldModel inModel, Context context) {
        return StreamSupport.stream(mappingInput.read(inModel, context).spliterator(), false)
                .filter(t -> converter.convert(inModel, context, t))
                .collect(Collectors.toList());
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return true;
    }
}
