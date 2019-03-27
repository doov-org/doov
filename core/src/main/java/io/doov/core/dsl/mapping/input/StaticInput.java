/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping.input;

import java.util.function.Supplier;

import io.doov.core.FieldModel;
import io.doov.core.Try;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.StaticMetadata;

public class StaticInput<T> implements ContextAccessor<T> {

    private final Supplier<T> valueSupplier;
    private final StaticMetadata<T> metadata;

    public StaticInput(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
        this.metadata = StaticMetadata.create(valueSupplier);
    }

    public StaticInput(T value) {
        this.valueSupplier = () -> value;
        this.metadata = StaticMetadata.create(valueSupplier);
    }

    @Override
    public StaticMetadata metadata() {
        return metadata;
    }

    @Override
    public Try<T> value(FieldModel model, Context context) {
        return Try.supplied(valueSupplier);
    }
}
