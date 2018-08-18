/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping.builder;

import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.*;

public class ContextawareStepMap<I> {

    private MappingInput<I> input;

    private ContextawareStepMap(MappingInput<I> input) {
        this.input = input;
    }

    public ContextawareStepMap(BiFunction<DslModel, Context, I> valueFunction) {
        input = new FunctionInput<>(valueFunction);
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
    public <O> ContextawareStepMap<O> using(TypeConverter<I, O> typeConverter) {
        return new ContextawareStepMap<>(new ConverterInput<>(input, typeConverter));
    }
}
