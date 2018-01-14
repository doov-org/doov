package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

public interface StepMapping<I, O> extends Readable, SyntaxTree {

     SimpleMappingRule<I, O> to(DslField<O> outFieldInfo);
}
