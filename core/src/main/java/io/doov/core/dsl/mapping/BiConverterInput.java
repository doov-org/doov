/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class BiConverterInput<U, S, T> implements MappingInput<T> {

    private final MappingMetadata metadata;
    private final MappingInput<U> mappingInput1;
    private final MappingInput<S> mappingInput2;
    private final BiTypeConverter<U, S, T> converter;

    public BiConverterInput(MappingInput<U> mappingInput1, MappingInput<S> mappingInput2,
            BiTypeConverter<U, S, T> converter) {
        this.mappingInput1 = mappingInput1;
        this.mappingInput2 = mappingInput2;
        this.converter = converter;
        this.metadata = MappingMetadata.metadataInput(mappingInput1.getMetadata(), mappingInput2.getMetadata());
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return mappingInput1.validate(inModel) && mappingInput2.validate(inModel);
    }

    @Override
    public MappingMetadata getMetadata() {
        return metadata;
    }

    @Override
    public T read(DslModel inModel, Context context) {
        return converter.convert(inModel, context,
                mappingInput1.read(inModel, context),
                mappingInput2.read(inModel, context));
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
        converter.accept(visitor, depth);
    }
}
