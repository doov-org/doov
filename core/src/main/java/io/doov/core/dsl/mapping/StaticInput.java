/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.Optional;
import java.util.function.Supplier;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.StaticMetadata;

public class StaticInput<T> extends AbstractDSLBuilder implements ContextAccessor<T> {

    private final Supplier<T> valueSupplier;
    private final StaticMetadata<T> metadata;

    public StaticInput(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
        this.metadata = StaticMetadata.create(valueSupplier);
    }

    @Override
    public boolean validate(FieldModel model) {
        return true;
    }

    @Override
    public StaticMetadata metadata() {
        return metadata;
    }

    @Override
    public Optional<T> value(FieldModel model, Context context) {
        return Optional.ofNullable(valueSupplier.get());
    }
}
