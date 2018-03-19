/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.StaticTypeConverter;

/**
 * Intermediary step for creating mapping rule.
 * Associates in value with type {@link I} with the out field with type {@link O}
 *
 * @param <I> in type
 * @param <O> out type
 */
public class StaticStepMapping<I, O> {

    private final Supplier<I> inputObject;
    private final StaticTypeConverter<I, O> typeConverter;

    StaticStepMapping(Supplier<I> inputObject, StaticTypeConverter<I, O> typeConverter) {
        this.inputObject = inputObject;
        this.typeConverter = typeConverter;
    }

    /**
     * Return the mapping rule
     *
     * @param outFieldInfo out field
     * @return static mapping rule
     */
    public StaticMappingRule<I, O> to(DslField<O> outFieldInfo) {
        return new StaticMappingRule<>(inputObject, outFieldInfo, typeConverter);
    }

}
