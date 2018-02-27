package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

public interface StepMap<I> extends Readable, SyntaxTree {

    <O> StepMapping<I, O> using(TypeConverter<I, O> typeConverter);

    SimpleMappingRule<I, I> to(DslField<I> outFieldInfo);
}
