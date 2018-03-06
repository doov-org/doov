package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * First step for creating mapping rule.
 * Associates the in field with type {@link I} with a converter or the out field with type {@link I}
 *
 * @param <I> in type
 */
public interface StepMap<I> extends Readable, SyntaxTree {

    /**
     * Return the step mapping
     *
     * @param typeConverter type converter
     * @param <O>           out type
     * @return the step mapping
     */
    <O> StepMapping<I, O> using(TypeConverter<I, O> typeConverter);

    /**
     * Return the mapping rule
     *
     * @param outFieldInfo out field info
     * @return the mapping rule
     */
    SimpleMappingRule<I, I> to(DslField<I> outFieldInfo);
}
