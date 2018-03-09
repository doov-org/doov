/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;

public class DefaultStaticStepMap<I> implements StaticStepMap<I> {

    private final Supplier<I> inputObject;

    public DefaultStaticStepMap(Supplier<I> input) {
        this.inputObject = input;
    }

    @Override
    public StaticMappingRule<I, I> to(DslField<I> outFieldInfo) {
        return new StaticMappingRule<>(inputObject, outFieldInfo, DefaultStaticTypeConverter.identity());
    }

    @Override
    public <O> StepMapping<I, O> using(StaticTypeConverter<I, O> typeConverter) {
        return new StaticSimpleStepMapping<>(inputObject, typeConverter);
    }

}
