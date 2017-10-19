/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.isMetadata;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.Optional;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.BooleanFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public class BooleanCondition extends DefaultCondition<BooleanFieldInfo, Boolean> {

    public BooleanCondition(BooleanFieldInfo field) {
        super(field);
    }

    public BooleanCondition(FieldMetadata metadata, Function<FieldModel, Optional<Boolean>> value) {
        super(metadata, value);
    }

    public final StepCondition isTrue() {
        return step(isMetadata(field, true),
                        model -> Optional.of(TRUE),
                        Boolean::equals);
    }

    public final StepCondition isFalse() {
        return step(isMetadata(field, false),
                        model -> Optional.of(FALSE),
                        Boolean::equals);
    }

}
