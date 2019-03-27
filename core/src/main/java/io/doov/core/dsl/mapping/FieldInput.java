/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.Optional;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;

public class FieldInput<T> extends AbstractDSLBuilder implements ContextAccessor<T> {

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
    public MappingMetadata metadata() {
        return metadata;
    }

    @Override
    public Optional<T> value(FieldModel model, Context context) {
        return Optional.ofNullable(model.get(field));
    }
}
