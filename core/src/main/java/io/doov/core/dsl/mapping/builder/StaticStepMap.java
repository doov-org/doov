/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping.builder;

import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.*;

/**
 * First step for creating a static mapping rule.
 *
 * @param <I> in type
 */
public class StaticStepMap<I> {

    private final MappingInput<I> input;

    public StaticStepMap(Supplier<I> input) {
        this(new StaticInput<>(input));
    }

    private StaticStepMap(MappingInput<I> input) {
        this.input = input;
    }

    /**
     * Return the static mapping rule
     *
     * @param outFieldInfo out field info
     * @return the static mapping rule
     */
    public DefaultMappingRule<I> to(DslField<I> outFieldInfo) {
        return new DefaultMappingRule<>(input, new FieldOutput<>(outFieldInfo));
    }
    /**
     * Return the static mapping rule
     *
     * @param consumer consumer
     * @return the static mapping rule
     */
    public DefaultMappingRule<I> to(TriConsumer<DslModel, Context, I> consumer) {
        return new DefaultMappingRule<>(input, new ConsumerOutput<>(consumer));
    }

    /**
     * Return the step mapping
     *
     * @param typeConverter static type converter
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> StaticStepMap<O> using(TypeConverter<I, O> typeConverter) {
        return new StaticStepMap<>(new ConverterInput<>(input, typeConverter));
    }

}
