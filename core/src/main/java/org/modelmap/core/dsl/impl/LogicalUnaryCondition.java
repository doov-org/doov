/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import static org.modelmap.core.dsl.meta.UnaryMetadata.NOT;

import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.meta.UnaryMetadata;

public class LogicalUnaryCondition extends AbstractStepCondition {

    private LogicalUnaryCondition(UnaryMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static LogicalUnaryCondition negate(StepCondition value) {
        return new LogicalUnaryCondition(new UnaryMetadata(NOT, value),
                        fieldContext -> value.predicate().negate().test(fieldContext));
    }

}
