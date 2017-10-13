/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import static org.modelmap.core.dsl.meta.FieldMetadata.GREATER_OR_EQUALS;
import static org.modelmap.core.dsl.meta.FieldMetadata.GREATER_THAN;
import static org.modelmap.core.dsl.meta.FieldMetadata.LESSER_OR_EQUALS;
import static org.modelmap.core.dsl.meta.FieldMetadata.LESSER_THAN;

import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.FloatFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class FloatCondition extends AbstractStepCondition {

    private FloatCondition(FieldMetadata<FloatFieldInfo, Float> metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static FloatCondition lesserThan(FloatFieldInfo field, float value) {
        return new FloatCondition(new FieldMetadata<>(field, LESSER_THAN, value),
                        fieldContext -> value(fieldContext, field).map(v -> v < value).orElse(false));
    }

    public static FloatCondition lesserOrEquals(FloatFieldInfo field, float value) {
        return new FloatCondition(new FieldMetadata<>(field, LESSER_OR_EQUALS, value),
                        fieldContext -> value(fieldContext, field).map(v -> v <= value).orElse(false));
    }

    public static FloatCondition greaterThan(FloatFieldInfo field, float value) {
        return new FloatCondition(new FieldMetadata<>(field, GREATER_THAN, value),
                        fieldContext -> value(fieldContext, field).map(v -> v > value).orElse(false));
    }

    public static FloatCondition greaterOrEquals(FloatFieldInfo field, float value) {
        return new FloatCondition(new FieldMetadata<>(field, GREATER_OR_EQUALS, value),
                        fieldContext -> value(fieldContext, field).map(v -> v >= value).orElse(false));
    }

    public static Optional<Float> value(FieldModel fieldModel, FloatFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Float> get(field.id()));
    }

}
