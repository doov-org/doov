package io.doov.core.dsl.lang;

/**
 * First step for creating a n-ary mapping rule.
 * Associates the in fields with with a generic converter
 */
public interface NaryStepMap {

    /**
     * Return the step mapping
     *
     * @param typeConverter type converter
     * @param <O>           out type
     * @return the step mapping
     */
    <O> NaryStepMapping<O> using(GenericTypeConverter<O> typeConverter);
}
