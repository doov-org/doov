/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class TypeCondition<T> extends AbstractStepCondition {

    private TypeCondition(FieldMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static <T> TypeCondition<T> eq(FieldInfo field, T value) {
        return new TypeCondition<>(FieldMetadata.equals(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v.equals(value)).orElse(false));
    }

    public static <T> TypeCondition<T> notEq(FieldInfo field, T value) {
        return new TypeCondition<>(FieldMetadata.notEquals(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> !v.equals(value)).orElse(false));
    }

    public static <T> TypeCondition<T> isNull(FieldInfo field) {
        return new TypeCondition<>(FieldMetadata.isNull(field, null),
                        fieldContext -> !value(fieldContext, field).isPresent());
    }

    public static <T> TypeCondition<T> isNotNull(FieldInfo field) {
        return new TypeCondition<>(FieldMetadata.isNotNull(field, null),
                        fieldContext -> value(fieldContext, field).isPresent());
    }

    public static <T> Optional<T> value(FieldModel fieldModel, FieldInfo field) {
        return Optional.ofNullable(fieldModel.<T> get(field.id()));
    }

}
