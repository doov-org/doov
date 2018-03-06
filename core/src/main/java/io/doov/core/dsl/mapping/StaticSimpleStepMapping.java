/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MetadataVisitor;

public class StaticSimpleStepMapping<I, O> implements StepMapping<I, O> {

    private final Supplier<I> inputObject;
    private final StaticTypeConverter<I, O> typeConverter;

    public StaticSimpleStepMapping(Supplier<I> inputObject, StaticTypeConverter<I, O> typeConverter) {
        this.inputObject = inputObject;
        this.typeConverter = typeConverter;
    }

    @Override
    public SimpleMappingRule<I, O> to(DslField<O> outFieldInfo) {
        return new StaticMappingRule<>(inputObject, outFieldInfo, typeConverter);
    }

}
