/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import static org.modelmap.core.dsl.meta.BinaryMetadata.AND;
import static org.modelmap.core.dsl.meta.BinaryMetadata.OR;

import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.meta.BinaryMetadata;

public class LogicalBinaryCondition implements StepCondition {

    private final BinaryMetadata metadata;
    private final Predicate<FieldModel> predicate;

    private LogicalBinaryCondition(BinaryMetadata metadata, Predicate<FieldModel> predicate) {
        this.metadata = metadata;
        this.predicate = predicate;
    }

    @Override
    public Predicate<FieldModel> predicate() {
        return predicate;
    }

    @Override
    public String readable() {
        return metadata.readable();
    }

    public static LogicalBinaryCondition and(StepCondition left, StepCondition right) {
        return new LogicalBinaryCondition(new BinaryMetadata(left, AND, right),
                        fieldContext -> left.predicate().and(right.predicate()).test(fieldContext));
    }

    public static LogicalBinaryCondition or(StepCondition left, StepCondition right) {
        return new LogicalBinaryCondition(new BinaryMetadata(left, OR, right),
                        fieldContext -> left.predicate().or(right.predicate()).test(fieldContext));
    }

}
