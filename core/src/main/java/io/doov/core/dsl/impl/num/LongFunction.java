/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.num;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class LongFunction extends NumericFunction<Long> implements LongOperators {

    public LongFunction(DslField<Long> field) {
        super(field);
    }

    public LongFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<Long>> value) {
        super(metadata, value);
    }

    @Override
    protected LongFunction numericFunction(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<Long>> value) {
        return new LongFunction(metadata, value);
    }

}
