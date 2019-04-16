/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.grammar.ASTNode;
import io.doov.core.dsl.grammar.leaf.FieldValue;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.MappingOutput;
import io.doov.core.dsl.meta.MappingMetadata;

public class FieldOutput<T> implements MappingOutput<T> {

    private final DslField<T> field;
    private final MappingMetadata metadata;

    public FieldOutput(DslField<T> field) {
        this.field = field;
        this.metadata = MappingMetadata.fieldOutput(field);
    }

    @Override
    public boolean validate(FieldModel outModel) {
        return outModel.getFieldInfos().stream().anyMatch(f -> f.id() == field.id());
    }

    @Override
    public ASTNode<T> ast() {
        return new FieldValue<>(field);
    }

    @Override
    public MappingMetadata metadata() {
        return metadata;
    }

    @Override
    public void write(FieldModel outModel, Context context, T value) {
        outModel.set(field, value);
    }

}
