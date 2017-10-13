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
import org.modelmap.core.dsl.field.IntegerFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class IntegerCondition extends AbstractStepCondition {

    private IntegerCondition(FieldMetadata<IntegerFieldInfo, Integer> metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static IntegerCondition lesserThan(IntegerFieldInfo field, int value) {
        return new IntegerCondition(new FieldMetadata<>(field, LESSER_THAN, value),
                        fieldContext -> value(fieldContext, field).map(v -> v < value).orElse(false));
    }

    public static IntegerCondition lesserOrEquals(IntegerFieldInfo field, int value) {
        return new IntegerCondition(new FieldMetadata<>(field, LESSER_OR_EQUALS, value),
                        fieldContext -> value(fieldContext, field).map(v -> v <= value).orElse(false));
    }

    public static IntegerCondition greaterThan(IntegerFieldInfo field, int value) {
        return new IntegerCondition(new FieldMetadata<>(field, GREATER_THAN, value),
                        fieldContext -> value(fieldContext, field).map(v -> v > value).orElse(false));
    }

    public static IntegerCondition greaterOrEquals(IntegerFieldInfo field, int value) {
        return new IntegerCondition(new FieldMetadata<>(field, GREATER_OR_EQUALS, value),
                        fieldContext -> value(fieldContext, field).map(v -> v >= value).orElse(false));
    }

    public static Optional<Integer> value(FieldModel fieldModel, IntegerFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Integer> get(field.id()));
    }

}
