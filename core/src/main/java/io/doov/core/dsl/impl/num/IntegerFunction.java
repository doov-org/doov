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

public class IntegerFunction extends NumericFunction<Integer> implements IntegerOperators {

    public IntegerFunction(DslField<Integer> field) {
        super(field);
    }

    public IntegerFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<Integer>> value) {
        super(metadata, value);
    }

    public IntegerFunction(NumericCondition<Long> condition) {
        this(condition.getMetadata(),
                        (model, context) -> condition.getFunction().apply(model, context).map(Long::intValue));
    }

    @Override
    protected IntegerFunction numericCondition(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<Integer>> value) {
        return new IntegerFunction(metadata, value);
    }

}
