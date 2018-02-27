/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

public interface StaticStepMap<I> extends Readable, SyntaxTree {

    <O> StepMapping<I, O> using(StaticTypeConverter<I, O> typeConverter);

    SimpleMappingRule<I, I> to(DslField<I> outFieldInfo);
}