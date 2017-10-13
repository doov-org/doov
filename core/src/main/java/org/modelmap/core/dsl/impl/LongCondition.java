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
import org.modelmap.core.dsl.field.LongFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class LongCondition extends AbstractStepCondition {

    private LongCondition(FieldMetadata<LongFieldInfo, Long> metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static LongCondition lesserThan(LongFieldInfo field, long value) {
        return new LongCondition(new FieldMetadata<>(field, LESSER_THAN, value),
                        fieldContext -> value(fieldContext, field).map(v -> v < value).orElse(false));
    }

    public static LongCondition lesserOrEquals(LongFieldInfo field, long value) {
        return new LongCondition(new FieldMetadata<>(field, LESSER_OR_EQUALS, value),
                        fieldContext -> value(fieldContext, field).map(v -> v <= value).orElse(false));
    }

    public static LongCondition greaterThan(LongFieldInfo field, long value) {
        return new LongCondition(new FieldMetadata<>(field, GREATER_THAN, value),
                        fieldContext -> value(fieldContext, field).map(v -> v > value).orElse(false));
    }

    public static LongCondition greaterOrEquals(LongFieldInfo field, long value) {
        return new LongCondition(new FieldMetadata<>(field, GREATER_OR_EQUALS, value),
                        fieldContext -> value(fieldContext, field).map(v -> v >= value).orElse(false));
    }

    public static Optional<Long> value(FieldModel fieldModel, LongFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Long> get(field.id()));
    }

}
