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

public class LogicalBinaryCondition extends AbstractStepCondition {

    private LogicalBinaryCondition(BinaryMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
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
