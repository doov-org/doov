/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.Supplier;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;

public class StaticInput<T> extends AbstractDSLBuilder implements MappingInput<T> {

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
    public MappingMetadata metadata() {
        return metadata;
    }

    @Override
    public T read(FieldModel inModel, Context context) {
        return valueSupplier.get();
    }
}
