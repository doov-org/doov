/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;

/**
 * First step for creating a static mapping rule.
 *
 * @param <I> in type
 */
public class StaticStepMap<I> {

    private final Supplier<I> inputObject;

    public StaticStepMap(Supplier<I> input) {
        this.inputObject = input;
    }

    /**
     * Return the static mapping rule
     *
     * @param outFieldInfo out field info
     * @return the static mapping rule
     */
    public StaticMappingRule<I, I> to(DslField<I> outFieldInfo) {
        return new StaticMappingRule<>(inputObject, outFieldInfo, DefaultStaticTypeConverter.identity());
    }

    /**
     * Return the step mapping
     *
     * @param typeConverter static type converter
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> StaticStepMapping<I, O> using(StaticTypeConverter<I, O> typeConverter) {
        return new StaticStepMapping<>(inputObject, typeConverter);
    }

}
