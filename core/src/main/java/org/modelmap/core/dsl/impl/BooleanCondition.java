/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.BooleanFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class BooleanCondition extends AbstractStepCondition {

    private BooleanCondition(FieldMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static BooleanCondition isTrue(BooleanFieldInfo field) {
        return new BooleanCondition(FieldMetadata.isMetadata(field, true),
                        fieldContext -> value(fieldContext, field).orElse(false));
    }

    public static BooleanCondition isFalse(BooleanFieldInfo field) {
        return new BooleanCondition(FieldMetadata.isMetadata(field, false),
                        fieldContext -> value(fieldContext, field).map(v -> !v).orElse(false));
    }

    public static Optional<Boolean> value(FieldModel fieldModel, BooleanFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Boolean> get(field.id()));
    }

}
