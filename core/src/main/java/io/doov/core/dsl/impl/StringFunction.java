/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.function.StringFunctionMetadata.lengthIsMetadata;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.impl.num.IntegerFunction;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class StringFunction extends StringCondition {

    public StringFunction(DslField<String> field) {
        super(field);
    }

    public StringFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<String>> value) {
        super(metadata, value);
    }

    /**
     * Returns an integer condition that returns the node value length.
     *
     * @return the integer condition
     */
    public IntegerFunction length() {
        return new IntegerFunction(lengthIsMetadata(metadata),
                (model, context) -> value(model, context).map(String::length));
    }

    /**
     * Returns an integer condition that returns the node value as an integer.
     *
     * @return the integer condition
     */
    public IntegerFunction parseInt() {
        return new IntegerFunction(metadata,
                (model, context) -> value(model, context).map(Integer::parseInt));
    }
}
