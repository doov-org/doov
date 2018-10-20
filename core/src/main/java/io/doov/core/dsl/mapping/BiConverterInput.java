/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.MappingInputMetadata.inputMetadata;
import static io.doov.core.dsl.meta.MappingMetadata.metadataInput;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingInputMetadata;
import io.doov.core.dsl.meta.Metadata;

public class BiConverterInput<U, S, T> extends AbstractDSLBuilder implements MappingInput<T> {

    private final MappingInputMetadata metadata;
    private final MappingInput<U> mappingInput1;
    private final MappingInput<S> mappingInput2;
    private final BiTypeConverter<U, S, T> converter;

    public BiConverterInput(MappingInput<U> mappingInput1, MappingInput<S> mappingInput2,
                    BiTypeConverter<U, S, T> converter) {
        this.mappingInput1 = mappingInput1;
        this.mappingInput2 = mappingInput2;
        this.converter = converter;
        this.metadata = inputMetadata(metadataInput(mappingInput1.metadata(), mappingInput2.metadata()), converter.metadata());
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return mappingInput1.validate(inModel) && mappingInput2.validate(inModel);
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public T read(DslModel inModel, Context context) {
        return converter.convert(inModel, context, mappingInput1.read(inModel, context),
                        mappingInput2.read(inModel, context));
    }

}
