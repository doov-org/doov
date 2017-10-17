/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.BinaryMetadata.andMetadata;
import static io.doov.core.dsl.meta.BinaryMetadata.orMetadata;

import java.util.function.BiPredicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.BinaryMetadata;

public class LogicalBinaryCondition extends AbstractStepCondition {

    private LogicalBinaryCondition(BinaryMetadata metadata, BiPredicate<FieldModel, Context> predicate) {
        super(metadata, predicate);
    }

    public static LogicalBinaryCondition and(StepCondition left, StepCondition right) {
        return new LogicalBinaryCondition(andMetadata(left, right),
                        (model, context) -> left.predicate().and(right.predicate()).test(model, context));
    }

    public static LogicalBinaryCondition or(StepCondition left, StepCondition right) {
        return new LogicalBinaryCondition(orMetadata(left, right),
                        (model, context) -> left.predicate().or(right.predicate()).test(model, context));
    }

}
