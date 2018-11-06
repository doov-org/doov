/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.time;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class LocalDateTimeFunction extends TemporalFunction<LocalDateTime> implements LocalDateTimeOperators {

    public LocalDateTimeFunction(DslField<LocalDateTime> field) {
        super(field);
    }

    protected LocalDateTimeFunction(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalDateTime>> value) {
        super(metadata, value);
    }

    @Override
    protected LocalDateTimeFunction temporalFunction(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalDateTime>> value) {
        return new LocalDateTimeFunction(metadata, value);
    }
}
