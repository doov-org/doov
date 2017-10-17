/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.afterMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.beforeMetadata;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.BiPredicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.LocalDateFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public class LocalDateCondition extends AbstractStepCondition {

    private LocalDateCondition(FieldMetadata metadata, BiPredicate<FieldModel, Context> predicate) {
        super(metadata, predicate);
    }

    public static StepCondition after(LocalDateFieldInfo field, LocalDate value) {
        return new LocalDateCondition(afterMetadata(field, value),
                        (model, context) -> value(model, field).map(v -> v.isAfter(value)).orElse(false));
    }

    public static StepCondition before(LocalDateFieldInfo field, LocalDate value) {
        return new LocalDateCondition(beforeMetadata(field, value),
                        (model, context) -> value(model, field).map(v -> v.isBefore(value)).orElse(false));
    }

    public static StepCondition beforeOrEq(LocalDateFieldInfo field, LocalDate value) {
        return LogicalBinaryCondition.or(before(field, value), TypeCondition.eq(field, value));
    }

    public static StepCondition afterOrEq(LocalDateFieldInfo field, LocalDate value) {
        return LogicalBinaryCondition.or(after(field, value), TypeCondition.eq(field, value));
    }

    public static StepCondition between(LocalDateFieldInfo field, LocalDate minValue, LocalDate maxValue) {
        return LogicalBinaryCondition.and(beforeOrEq(field, maxValue), afterOrEq(field, minValue));
    }

    public static StepCondition notBetween(LocalDateFieldInfo field, LocalDate minValue, LocalDate maxValue) {
        return LogicalUnaryCondition.negate(between(field, minValue, maxValue));
    }

    public static Optional<LocalDate> value(FieldModel model, LocalDateFieldInfo field) {
        return Optional.ofNullable(model.<LocalDate> get(field.id()));
    }

}
