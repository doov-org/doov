/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.MappingInputMetadata.inputMetadata;

import java.util.List;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class NaryConverterInput<T> extends AbstractDSLBuilder implements MappingInput<T> {

    private final List<DslField<?>> fields;
    private final NaryTypeConverter<T> converter;
    private final MappingInputMetadata metadata;

    public NaryConverterInput(List<DslField<?>> fields, NaryTypeConverter<T> converter) {
        this.fields = fields;
        this.metadata = inputMetadata(converter.metadata(), fields.stream()
                .map(MappingMetadata::fieldInput)
                .toArray(Metadata[]::new));
        this.converter = converter;
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return fields.stream().allMatch(f -> inModel.getFieldIds().contains(f.id()));
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public T read(FieldModel inModel, Context context) {
        return converter.convert(inModel, context, fields.toArray(new DslField[0]));
    }

}
