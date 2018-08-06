/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StaticTypeConverter;

public class ContextawareStepMapping<I, O> {

    private final BiFunction<DslModel, Context, I> valueFunction;
    private final StaticTypeConverter<I, O> typeConverter;

    ContextawareStepMapping(BiFunction<DslModel, Context, I> valueFunction, StaticTypeConverter<I, O> typeConverter) {
        this.valueFunction = valueFunction;
        this.typeConverter = typeConverter;
    }

    /**
     * Return the mapping rule
     *
     * @param outFieldInfo out field
     * @return static mapping rule
     */
    public ContextawareMappingRule<I, O> to(DslField<O> outFieldInfo) {
        return new ContextawareMappingRule<>(valueFunction, outFieldInfo, typeConverter);
    }
}
