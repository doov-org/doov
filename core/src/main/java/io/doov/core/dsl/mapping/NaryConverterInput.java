/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.List;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class NaryConverterInput<T> extends AbstractDSLBuilder implements MappingInput<T> {

    private final List<DslField> fields;
    private final NaryTypeConverter<T> converter;
    private final MappingMetadata metadata;

    public NaryConverterInput(List<DslField> fields, NaryTypeConverter<T> converter) {
        this.fields = fields;
        this.metadata = MappingMetadata.fieldsInput(fields);
        this.converter = converter;
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return fields.stream().allMatch(f -> inModel.getFieldIds().contains(f.id()));
    }

    @Override
    public MappingMetadata metadata() {
        return metadata;
    }

    @Override
    public T read(DslModel inModel, Context context) {
        return converter.convert(inModel, context, fields.toArray(new DslField[0]));
    }

    @Deprecated
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
        converter.metadata().accept(visitor, depth);
    }
}
