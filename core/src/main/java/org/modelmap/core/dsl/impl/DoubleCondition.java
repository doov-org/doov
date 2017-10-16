/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.DoubleFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class DoubleCondition extends AbstractStepCondition {

    private DoubleCondition(FieldMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static DoubleCondition lesserThan(DoubleFieldInfo field, double value) {
        return new DoubleCondition(FieldMetadata.lesserThan(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v < value).orElse(false));
    }

    public static DoubleCondition lesserOrEquals(DoubleFieldInfo field, double value) {
        return new DoubleCondition(FieldMetadata.lesserOrEquals(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v <= value).orElse(false));
    }

    public static DoubleCondition greaterThan(DoubleFieldInfo field, double value) {
        return new DoubleCondition(FieldMetadata.greaterThan(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v > value).orElse(false));
    }

    public static DoubleCondition greaterOrEquals(DoubleFieldInfo field, double value) {
        return new DoubleCondition(FieldMetadata.greaterOrEquals(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v >= value).orElse(false));
    }

    public static Optional<Double> value(FieldModel fieldModel, DoubleFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Double> get(field.id()));
    }

}
