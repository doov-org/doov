/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.List;
import java.util.Objects;
import java.util.stream.*;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class MapEachInput<I, T extends Iterable<I>, O, U extends Iterable<O>>
        extends AbstractDSLBuilder implements MappingInput<U> {

    private final MappingInput<T> input;
    private final TypeConverter<I, O> converter;
    private final MapEachMetadata metadata;

    public MapEachInput(MappingInput<T> sourceInput, TypeConverter<I, O> converter) {
        this.input = sourceInput;
        this.converter = converter;
        this.metadata = MapEachMetadata.create(sourceInput.metadata(), converter.metadata());
    }

    @Override
    public MapEachMetadata metadata() {
        return metadata;
    }

    @Override
    public U read(FieldModel inModel, Context context) {
        List<O> collect = StreamSupport.stream(input.read(inModel, context).spliterator(), false)
                .map(i -> converter.convert(inModel, context, i))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return (U) collect;
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return true;
    }
}
