/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.UnaryMetadata.notMetadata;

import java.util.function.BiPredicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.UnaryMetadata;

public class LogicalUnaryCondition extends AbstractStepCondition {

    private LogicalUnaryCondition(UnaryMetadata metadata, BiPredicate<FieldModel, Context> predicate) {
        super(metadata, predicate);
    }

    public static LogicalUnaryCondition negate(StepCondition step) {
        return new LogicalUnaryCondition(notMetadata(step),
                        (model, context) -> step.predicate().negate().test(model, context));
    }

}
