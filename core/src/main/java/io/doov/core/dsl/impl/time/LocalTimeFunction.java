/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.time;

import java.time.LocalTime;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class LocalTimeFunction extends TemporalFunction<LocalTime> implements LocalTimeOperators {

    public LocalTimeFunction(DslField<LocalTime> field) {
        super(field);
    }

    protected LocalTimeFunction(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalTime>> value) {
        super(metadata, value);
    }

    @Override
    protected LocalTimeFunction temporalFunction(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalTime>> value) {
        return new LocalTimeFunction(metadata, value);
    }
}
