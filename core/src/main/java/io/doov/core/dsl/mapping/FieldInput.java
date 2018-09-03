/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.MappingInput;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class FieldInput<T> implements MappingInput<T> {

    private final MappingMetadata metadata;
    private final DslField<T> field;

    public FieldInput(DslField<T> field) {
        this.field = field;
        this.metadata = MappingMetadata.fieldInput(field);
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return inModel.getFieldInfos().stream().anyMatch(f -> f.id() == field.id());
    }

    @Override
    public MappingMetadata getMetadata() {
        return metadata;
    }

    @Override
    public T read(DslModel inModel, Context context) {
        return inModel.get(field);
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
    }

}
