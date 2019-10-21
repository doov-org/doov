/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.Supplier;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.StaticMetadata;

public class StaticInput<T> extends AbstractDSLBuilder implements MappingInput<T> {

    private final Supplier<T> valueSupplier;
    private final StaticMetadata<T> metadata;

    public StaticInput(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
        this.metadata = StaticMetadata.mappingLeaf(valueSupplier);
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return true;
    }

    @Override
    public StaticMetadata metadata() {
        return metadata;
    }

    @Override
    public T read(FieldModel inModel, Context context) {
        return valueSupplier.get();
    }
}
