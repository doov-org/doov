/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.time;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class LocalDateFunction extends TemporalFunction<LocalDate> implements LocalDateOperators {

    public LocalDateFunction(DslField<LocalDate> field) {
        super(field);
    }

    public LocalDateFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<LocalDate>> value) {
        super(metadata, value);
    }

    @Override
    protected LocalDateFunction temporalFunction(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalDate>> value) {
        return new LocalDateFunction(metadata, value);
    }
}
