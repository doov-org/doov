/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.LocalDateFieldInfo;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class LocalDateCondition extends AbstractStepCondition {

    private LocalDateCondition(FieldMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static StepCondition after(LocalDateFieldInfo field, LocalDate value) {
        return new LocalDateCondition(FieldMetadata.after(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v.isAfter(value)).orElse(false));
    }

    public static StepCondition before(LocalDateFieldInfo field, LocalDate value) {
        return new LocalDateCondition(FieldMetadata.before(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v.isBefore(value)).orElse(false));
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

    public static Optional<LocalDate> value(FieldModel fieldModel, LocalDateFieldInfo field) {
        return Optional.ofNullable(fieldModel.<LocalDate> get(field.id()));
    }

}
