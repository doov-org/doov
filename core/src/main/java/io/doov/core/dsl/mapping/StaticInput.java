/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.Supplier;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.MappingInput;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class StaticInput<T> implements MappingInput<T> {

    private final Supplier<T> valueSupplier;
    private final MappingMetadata metadata;

    public StaticInput(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
        this.metadata = MappingMetadata.valueInput(valueSupplier);
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return true;
    }

    @Override
    public MappingMetadata getMetadata() {
        return metadata;
    }

    @Override
    public T read(DslModel inModel, Context context) {
        return valueSupplier.get();
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
    }
}
