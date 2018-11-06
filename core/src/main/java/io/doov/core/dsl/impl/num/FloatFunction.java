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

public class FloatFunction extends NumericFunction<Float> implements FloatOperators {

    public FloatFunction(DslField<Float> field) {
        super(field);
    }

    public FloatFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<Float>> value) {
        super(metadata, value);
    }

    @Override
    protected FloatFunction numericCondition(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<Float>> value) {
        return new FloatFunction(metadata, value);
    }

}
