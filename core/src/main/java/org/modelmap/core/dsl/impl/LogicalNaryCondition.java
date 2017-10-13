/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import static java.util.Arrays.stream;
import static org.modelmap.core.dsl.meta.NaryMetadata.MATCH_ALL;
import static org.modelmap.core.dsl.meta.NaryMetadata.MATCH_ANY;
import static org.modelmap.core.dsl.meta.NaryMetadata.MATCH_NONE;

import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.meta.NaryMetadata;

public class LogicalNaryCondition extends AbstractStepCondition {

    private LogicalNaryCondition(NaryMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static LogicalNaryCondition matchAny(StepCondition... values) {
        return new LogicalNaryCondition(new NaryMetadata(MATCH_ANY, values),
                        fieldContext -> stream(values).anyMatch(s -> s.predicate().test(fieldContext)));
    }

    public static LogicalNaryCondition matchAll(StepCondition... values) {
        return new LogicalNaryCondition(new NaryMetadata(MATCH_ALL, values),
                        fieldContext -> stream(values).allMatch(s -> s.predicate().test(fieldContext)));
    }

    public static LogicalNaryCondition matchNone(StepCondition... values) {
        return new LogicalNaryCondition(new NaryMetadata(MATCH_NONE, values),
                        fieldContext -> stream(values).noneMatch(s -> s.predicate().test(fieldContext)));
    }

}
