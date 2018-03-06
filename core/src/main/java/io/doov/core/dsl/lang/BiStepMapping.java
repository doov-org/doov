package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;

/**
 * Intermediary step for creating a bi mapping rule.
 * Associates in fields with type {@link I} and {@link J} with the out field with type {@link O}
 *
 * @param <I> 1st in type
 * @param <J> 2nd in type
 * @param <O> out type
 */
public interface BiStepMapping<I, J, O> {

    /**
     * Return the bi mapping rule
     *
     * @param outFieldInfo out field
     * @return bi mapping rule
     */
    BiMappingRule<I, J, O> to(DslField<O> outFieldInfo);

}
