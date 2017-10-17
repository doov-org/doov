/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.equalsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.notEqualsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.notNullMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.nullMetadata;

import java.util.Optional;
import java.util.function.BiPredicate;

import io.doov.core.FieldInfo;
import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.FieldMetadata;

public class TypeCondition<T> extends AbstractStepCondition {

    private TypeCondition(FieldMetadata metadata, BiPredicate<FieldModel, Context> predicate) {
        super(metadata, predicate);
    }

    public static <T> TypeCondition<T> eq(FieldInfo field, T value) {
        return new TypeCondition<>(equalsMetadata(field, value),
                        (model, context) -> value(model, field).map(v -> v.equals(value)).orElse(false));
    }

    public static <T> TypeCondition<T> notEq(FieldInfo field, T value) {
        return new TypeCondition<>(notEqualsMetadata(field, value),
                        (model, context) -> value(model, field).map(v -> !v.equals(value)).orElse(false));
    }

    public static <T> TypeCondition<T> isNull(FieldInfo field) {
        return new TypeCondition<>(nullMetadata(field, null),
                        (model, context) -> !value(model, field).isPresent());
    }

    public static <T> TypeCondition<T> isNotNull(FieldInfo field) {
        return new TypeCondition<>(notNullMetadata(field, null),
                        (model, context) -> value(model, field).isPresent());
    }

    public static <T> Optional<T> value(FieldModel fieldModel, FieldInfo field) {
        return Optional.ofNullable(fieldModel.<T> get(field.id()));
    }

}
