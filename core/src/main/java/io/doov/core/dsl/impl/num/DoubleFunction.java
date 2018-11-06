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

public class DoubleFunction extends NumericFunction<Double> implements DoubleOperators {

    public DoubleFunction(DslField<Double> field) {
        super(field);
    }

    public DoubleFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<Double>> value) {
        super(metadata, value);
    }

    @Override
    protected DoubleFunction numericCondition(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<Double>> value) {
        return new DoubleFunction(metadata, value);
    }
}
