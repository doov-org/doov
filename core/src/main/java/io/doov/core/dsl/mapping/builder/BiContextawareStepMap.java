/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping.builder;

import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.BiConverterInput;
import io.doov.core.dsl.mapping.FunctionInput;

public class BiContextawareStepMap<I, J> {
    private MappingInput<I> input;
    private MappingInput<J> input2;

    public BiContextawareStepMap(MappingInput<I> input, MappingInput<J> input2) {
        this.input = input;
        this.input2 = input2;
    }

    public BiContextawareStepMap(
            BiFunction<FieldModel, Context, I> valueFunction,
            BiFunction<FieldModel, Context, J> valueFunction2) {
        this(new FunctionInput<>(valueFunction), new FunctionInput<>(valueFunction2));
    }

    /**
     * Return the step mapping
     *
     * @param typeConverter static type converter
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> ContextawareStepMap<O> using(BiTypeConverter<I, J, O> typeConverter) {
        return new ContextawareStepMap<>(new BiConverterInput<>(input, input2, typeConverter));
    }
}
