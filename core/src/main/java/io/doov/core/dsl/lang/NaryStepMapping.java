package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

public interface NaryStepMapping<O> extends Readable, SyntaxTree {

    NaryMappingRule<O> to(DslField<O> outFieldInfo);

}
