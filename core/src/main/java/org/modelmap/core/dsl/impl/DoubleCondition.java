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
import org.modelmap.core.dsl.field.DoubleFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class DoubleCondition extends AbstractStepCondition<DoubleFieldInfo, Double> {

    private DoubleCondition(FieldMetadata<DoubleFieldInfo, Double> metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    @Override
    public Predicate<FieldModel> predicate() {
        return predicate;
    }

    @Override
    public String readable() {
        return metadata.readable();
    }

    public static DoubleCondition lesserThan(DoubleFieldInfo field, double value) {
        return new DoubleCondition(new FieldMetadata<>(field, LESSER_THAN, value),
                        fieldContext -> value(fieldContext, field).map(v -> v < value).orElse(false));
    }

    public static DoubleCondition lesserOrEquals(DoubleFieldInfo field, double value) {
        return new DoubleCondition(new FieldMetadata<>(field, LESSER_OR_EQUALS, value),
                        fieldContext -> value(fieldContext, field).map(v -> v <= value).orElse(false));
    }

    public static DoubleCondition greaterThan(DoubleFieldInfo field, double value) {
        return new DoubleCondition(new FieldMetadata<>(field, GREATER_THAN, value),
                        fieldContext -> value(fieldContext, field).map(v -> v > value).orElse(false));
    }

    public static DoubleCondition greaterOrEquals(DoubleFieldInfo field, double value) {
        return new DoubleCondition(new FieldMetadata<>(field, GREATER_OR_EQUALS, value),
                        fieldContext -> value(fieldContext, field).map(v -> v >= value).orElse(false));
    }

    public static Optional<Double> value(FieldModel fieldModel, DoubleFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Double> get(field.id()));
    }

}
