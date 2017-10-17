/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.NaryMetadata.matchAllMetadata;
import static io.doov.core.dsl.meta.NaryMetadata.matchAnyMetadata;
import static io.doov.core.dsl.meta.NaryMetadata.matchNoneMetadata;
import static java.util.Arrays.stream;

import java.util.function.Predicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.NaryMetadata;

public class LogicalNaryCondition extends AbstractStepCondition {

    private LogicalNaryCondition(NaryMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static LogicalNaryCondition matchAny(StepCondition... steps) {
        return new LogicalNaryCondition(matchAnyMetadata(steps),
                        fieldContext -> stream(steps).anyMatch(s -> s.predicate().test(fieldContext)));
    }

    public static LogicalNaryCondition matchAll(StepCondition... steps) {
        return new LogicalNaryCondition(matchAllMetadata(steps),
                        fieldContext -> stream(steps).allMatch(s -> s.predicate().test(fieldContext)));
    }

    public static LogicalNaryCondition matchNone(StepCondition... steps) {
        return new LogicalNaryCondition(matchNoneMetadata(steps),
                        fieldContext -> stream(steps).noneMatch(s -> s.predicate().test(fieldContext)));
    }

}
