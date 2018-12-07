/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class BooleanFunction extends BooleanCondition {

    public BooleanFunction(DslField<Boolean> field) {
        super(field);
    }

    public BooleanFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<Boolean>> value) {
        super(metadata, value);
    }

}
