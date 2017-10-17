/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.isMetadata;

import java.util.Optional;
import java.util.function.BiPredicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.BooleanFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.FieldMetadata;

public class BooleanCondition extends AbstractStepCondition {

    private BooleanCondition(FieldMetadata metadata, BiPredicate<FieldModel, Context> predicate) {
        super(metadata, predicate);
    }

    public static BooleanCondition isTrue(BooleanFieldInfo field) {
        return new BooleanCondition(isMetadata(field, true),
                        (model, context) -> value(model, field).orElse(false));
    }

    public static BooleanCondition isFalse(BooleanFieldInfo field) {
        return new BooleanCondition(isMetadata(field, false),
                        (model, context) -> value(model, field).map(v -> !v).orElse(false));
    }

    public static Optional<Boolean> value(FieldModel fieldModel, BooleanFieldInfo field) {
        return Optional.ofNullable(fieldModel.<Boolean> get(field.id()));
    }

}
