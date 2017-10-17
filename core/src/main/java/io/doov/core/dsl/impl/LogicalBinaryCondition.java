/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.BinaryMetadata.andMetadata;
import static io.doov.core.dsl.meta.BinaryMetadata.orMetadata;

import java.util.function.Predicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.BinaryMetadata;

public class LogicalBinaryCondition extends AbstractStepCondition {

    private LogicalBinaryCondition(BinaryMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static LogicalBinaryCondition and(StepCondition left, StepCondition right) {
        return new LogicalBinaryCondition(andMetadata(left, right),
                        fieldContext -> left.predicate().and(right.predicate()).test(fieldContext));
    }

    public static LogicalBinaryCondition or(StepCondition left, StepCondition right) {
        return new LogicalBinaryCondition(orMetadata(left, right),
                        fieldContext -> left.predicate().or(right.predicate()).test(fieldContext));
    }

}
