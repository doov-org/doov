/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;

public class ContextawareStepMap<I> {

    private final BiFunction<DslModel, Context, I> valueFunction;

    public ContextawareStepMap(BiFunction<DslModel, Context, I> valueFunction) {
        this.valueFunction = valueFunction;
    }

    /**
     * Return the static mapping rule
     *
     * @param outFieldInfo out field info
     * @return the static mapping rule
     */
    public ContextawareMappingRule<I, I> to(DslField<I> outFieldInfo) {
        return new ContextawareMappingRule<>(valueFunction, outFieldInfo, DefaultStaticTypeConverter.identity());
    }

    /**
     * Return the step mapping
     *
     * @param typeConverter static type converter
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> ContextawareStepMapping<I, O> using(StaticTypeConverter<I, O> typeConverter) {
        return new ContextawareStepMapping<>(valueFunction, typeConverter);
    }
}
