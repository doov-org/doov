package io.doov.core.dsl.lang;

import io.doov.core.dsl.meta.SyntaxTree;

public interface NaryStepMap extends Readable, SyntaxTree {

    <O> NaryStepMapping<O> using(GenericTypeConverter<O> typeConverter);
}
