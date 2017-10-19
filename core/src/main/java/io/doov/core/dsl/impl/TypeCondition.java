/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.equalsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.notEqualsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.notNullMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.nullMetadata;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.DefaultFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public class TypeCondition<T> extends DefaultCondition<DefaultFieldInfo<T>, T> {

    public TypeCondition(DefaultFieldInfo<T> field) {
        super(field);
    }

    public TypeCondition(FieldMetadata metadata, Function<FieldModel, Optional<T>> value) {
        super(metadata, value);
    }

    // equals

    public final StepCondition eq(T value) {
        return step(equalsMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        Object::equals);
    }

    public final StepCondition eq(DefaultFieldInfo<T> value) {
        return step(equalsMetadata(field, value),
                        model -> value(model, value),
                        Object::equals);
    }

    // not equals

    public final StepCondition notEq(T value) {
        return step(notEqualsMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        (l, r) -> !l.equals(r));
    }

    public final StepCondition notEq(DefaultFieldInfo<T> value) {
        return step(notEqualsMetadata(field, value),
                        model -> value(model, value),
                        (l, r) -> !l.equals(r));
    }

    // null

    public final StepCondition isNull() {
        return step(nullMetadata(field, value), Objects::isNull);
    }

    // not null

    public final StepCondition isNotNull() {
        return step(notNullMetadata(field, value), obj -> !Objects.isNull(obj));
    }

}
