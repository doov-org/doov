package io.doov.core.dsl.lang;

import io.doov.core.dsl.meta.SyntaxTree;

/**
 * First step for creating a n-ary mapping rule.
 * Associates the in fields with with a generic converter
 */
public interface NaryStepMap extends Readable, SyntaxTree {

    /**
     * Return the step mapping
     *
     * @param typeConverter type converter
     * @param <O>           out type
     * @return the step mapping
     */
    <O> NaryStepMapping<O> using(GenericTypeConverter<O> typeConverter);
}
