package io.doov.core.dsl.lang;

import io.doov.core.dsl.meta.SyntaxTree;

public interface BiStepMap<I, J> extends Readable, SyntaxTree {

    <O> BiStepMapping<I, J, O> using(BiTypeConverter<I, J, O> typeConverter);

}
