/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.FloatFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class FloatCondition extends AbstractStepCondition {

    private FloatCondition(FieldMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static FloatCondition lesserThan(FloatFieldInfo field, float value) {
        return new FloatCondition(FieldMetadata.lesserThan(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v < value).orElse(false));
    }

    public static FloatCondition lesserOrEquals(FloatFieldInfo field, float value) {
        return new FloatCondition(FieldMetadata.lesserOrEquals(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v <= value).orElse(false));
    }

    public static FloatCondition greaterThan(FloatFieldInfo field, float value) {
        return new FloatCondition(FieldMetadata.greaterThan(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v > value).orElse(false));
    }

    public static FloatCondition greaterOrEquals(FloatFieldInfo field, float value) {
        return new FloatCondition(FieldMetadata.greaterOrEquals(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v >= value).orElse(false));
    }

    public static Optional<Float> value(FieldModel fieldModel, FloatFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Float> get(field.id()));
    }

}
