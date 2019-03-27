/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping.input;

import io.doov.core.FieldModel;
import io.doov.core.Try;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;

public class FieldInput<T> implements ContextAccessor<T> {

    private final MappingMetadata metadata;
    private final DslField<T> field;

    public FieldInput(DslField<T> field) {
        this.field = field;
        this.metadata = MappingMetadata.fieldInput(field);
    }

    public boolean validate(FieldModel inModel) {
        return inModel.getFieldInfos().stream().anyMatch(f -> f.id() == field.id());
    }

    @Override
    public MappingMetadata metadata() {
        return metadata;
    }

    @Override
    public Try<T> value(FieldModel model, Context context) {
        if(validate(model)) {
            return Try.supplied(() -> model.get(field));
        } else {
            return Try.failure(new Throwable("Field " + field + " not present."));
        }
    }
}
