/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import static org.modelmap.core.dsl.meta.FieldMetadata.IS_FALSE;
import static org.modelmap.core.dsl.meta.FieldMetadata.IS_TRUE;

import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.BooleanFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class BooleanCondition extends AbstractStepCondition {

    private BooleanCondition(FieldMetadata<BooleanFieldInfo, Boolean> metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static BooleanCondition isTrue(BooleanFieldInfo field, boolean value) {
        return new BooleanCondition(new FieldMetadata<>(field, IS_TRUE, value),
                        fieldContext -> value(fieldContext, field).orElse(false));
    }

    public static BooleanCondition isFalse(BooleanFieldInfo field, boolean value) {
        return new BooleanCondition(new FieldMetadata<>(field, IS_FALSE, value),
                        fieldContext -> value(fieldContext, field).map(v -> !v).orElse(false));
    }

    public static Optional<Boolean> value(FieldModel fieldModel, BooleanFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Boolean> get(field.id()));
    }

}
