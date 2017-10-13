/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import static org.modelmap.core.dsl.meta.FieldMetadata.EQUALS;
import static org.modelmap.core.dsl.meta.FieldMetadata.NOT_EQUALS;

import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class TypeCondition<T> extends AbstractStepCondition {

    private TypeCondition(FieldMetadata<FieldInfo, T> metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static <T> TypeCondition<T> eq(FieldInfo field, T value) {
        return new TypeCondition<>(new FieldMetadata<>(field, EQUALS, value),
                        fieldContext -> value(fieldContext, field).map(v -> v.equals(value)).orElse(false));
    }

    public static <T> TypeCondition<T> notEq(FieldInfo field, T value) {
        return new TypeCondition<>(new FieldMetadata<>(field, NOT_EQUALS, value),
                        fieldContext -> value(fieldContext, field).map(v -> !v.equals(value)).orElse(false));
    }

    public static <T> Optional<T> value(FieldModel fieldModel, FieldInfo field) {
        return Optional.ofNullable(fieldModel.<T> get(field.id()));
    }

}
