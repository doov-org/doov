/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import static org.modelmap.core.dsl.meta.FieldMetadata.IS;

import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.BooleanFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class BooleanCondition extends AbstractStepCondition {

    private BooleanCondition(FieldMetadata<BooleanFieldInfo, Boolean> metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static BooleanCondition isTrue(BooleanFieldInfo field) {
        return new BooleanCondition(new FieldMetadata<>(field, IS, true),
                        fieldContext -> value(fieldContext, field).orElse(false));
    }

    public static BooleanCondition isFalse(BooleanFieldInfo field) {
        return new BooleanCondition(new FieldMetadata<>(field, IS, false),
                        fieldContext -> value(fieldContext, field).map(v -> !v).orElse(false));
    }

    public static Optional<Boolean> value(FieldModel fieldModel, BooleanFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Boolean> get(field.id()));
    }

}
