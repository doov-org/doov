/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.mapping.StaticMappingRule;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * First step for creating a static mapping rule.
 *
 * @param <I> in type
 */
public interface StaticStepMap<I> extends Readable, SyntaxTree {

    /**
     * Return the step mapping
     *
     * @param typeConverter static type converter
     * @param <O>           out type
     * @return the step mapping
     */
    <O> StepMapping<I, O> using(StaticTypeConverter<I, O> typeConverter);

    /**
     * Return the static mapping rule
     *
     * @param outFieldInfo out field info
     * @return the static mapping rule
     */
    StaticMappingRule<I, I> to(DslField<I> outFieldInfo);
}