/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import static org.modelmap.core.dsl.meta.UnaryMetadata.NOT;

import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.meta.UnaryMetadata;

public class LogicalUnaryCondition implements StepCondition {

    private final UnaryMetadata metadata;
    private final Predicate<FieldModel> predicate;

    private LogicalUnaryCondition(UnaryMetadata metadata, Predicate<FieldModel> predicate) {
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

    public static LogicalUnaryCondition negate(StepCondition value) {
        return new LogicalUnaryCondition(new UnaryMetadata(NOT, value),
                        fieldContext -> value.predicate().negate().test(fieldContext));
    }

}
