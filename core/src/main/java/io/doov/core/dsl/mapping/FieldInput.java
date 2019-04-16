/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.grammar.ASTNode;
import io.doov.core.dsl.grammar.leaf.FieldValue;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;

public class FieldInput<T> extends AbstractDSLBuilder<T> implements MappingInput<T> {

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
    public T read(FieldModel inModel, Context context) {
        return inModel.get(field);
    }

    @Override
    public ASTNode<T> ast (){
        return new FieldValue<>(field);
    }
}
