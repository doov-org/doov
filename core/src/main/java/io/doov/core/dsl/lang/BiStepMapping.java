package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

public interface BiStepMapping<I, J, O> extends Readable, SyntaxTree {

    BiMappingRule<I, J, O> to(DslField<O> outFieldInfo);

}
