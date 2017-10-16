/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.LongFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class LongCondition extends AbstractStepCondition {

    private LongCondition(FieldMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static LongCondition lesserThan(LongFieldInfo field, long value) {
        return new LongCondition(FieldMetadata.lesserThan(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v < value).orElse(false));
    }

    public static LongCondition lesserOrEquals(LongFieldInfo field, long value) {
        return new LongCondition(FieldMetadata.lesserOrEquals(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v <= value).orElse(false));
    }

    public static LongCondition greaterThan(LongFieldInfo field, long value) {
        return new LongCondition(FieldMetadata.greaterThan(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v > value).orElse(false));
    }

    public static LongCondition greaterOrEquals(LongFieldInfo field, long value) {
        return new LongCondition(FieldMetadata.greaterOrEquals(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v >= value).orElse(false));
    }

    public static Optional<Long> value(FieldModel fieldModel, LongFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Long> get(field.id()));
    }

}
