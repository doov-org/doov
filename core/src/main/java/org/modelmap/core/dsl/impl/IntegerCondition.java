/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import java.util.Optional;
import java.util.function.Function;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.IntegerFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;
import org.modelmap.core.dsl.meta.Metadata;

public class IntegerCondition {

    private final IntegerFieldInfo field;
    private final FieldMetadata metadata;
    private final Function<FieldModel, Optional<Integer>> value;

    public IntegerCondition(IntegerFieldInfo field) {
        this.field = field;
        this.metadata = FieldMetadata.empty();
        this.value = fieldModel -> Optional.ofNullable(fieldModel.<Integer> get(field.id()));
    }

    public IntegerCondition(FieldMetadata metadata, Function<FieldModel, Optional<Integer>> value) {
        this.field = null;
        this.metadata = metadata;
        this.value = value;
    }

    public IntegerStepCondition lesserThan(int value) {
        return new IntegerStepCondition(metadata.merge(FieldMetadata.lesserThan(field, value)),
                        this.value, i -> i < value);
    }

    public IntegerStepCondition lesserOrEquals(int value) {
        return new IntegerStepCondition(metadata.merge(FieldMetadata.lesserOrEquals(field, value)),
                        this.value, i -> i <= value);
    }

    public IntegerStepCondition greaterThan(int value) {
        return new IntegerStepCondition(metadata.merge(FieldMetadata.greaterThan(field, value)),
                        this.value, i -> i > value);
    }

    public IntegerStepCondition greaterOrEquals(int value) {
        return new IntegerStepCondition(metadata.merge(FieldMetadata.greaterOrEquals(field, value)),
                        this.value, i -> i >= value);
    }

    public IntegerStepCondition between(int minIncluded, int maxExcluded) {
        return new IntegerStepCondition(metadata.merge(FieldMetadata.between(field, minIncluded, maxExcluded)),
                        this.value, i -> i >= minIncluded && i < maxExcluded);
    }

    private static class IntegerStepCondition extends AbstractStepCondition {

        IntegerStepCondition(Metadata metadata,
                        Function<FieldModel, Optional<Integer>> value,
                        Function<Integer, Boolean> predicate) {
            super(metadata, fieldModel -> value.apply(fieldModel).map(predicate).orElse(false));
        }

    }

}
