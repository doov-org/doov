package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Intermediary step for creating mapping rule.
 * Associates in field with type {@link I} with the out field with type {@link O}
 *
 * @param <I> in type
 * @param <O> out type
 */
public interface StepMapping<I, O> extends Readable, SyntaxTree {

    /**
     * Return the mapping rule
     *
     * @param outFieldInfo out field info
     * @return simple mapping rule
     */
    SimpleMappingRule<I, O> to(DslField<O> outFieldInfo);
}
