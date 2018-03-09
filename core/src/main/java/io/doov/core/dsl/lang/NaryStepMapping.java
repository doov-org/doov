package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Intermediary step for creating n-ary mapping rule.
 * Associates in fields with the out field with type {@link O}
 *
 * @param <O> out type
 */
public interface NaryStepMapping<O> extends Readable, SyntaxTree {

    /**
     * Return the n-ary mapping rule
     *
     * @param outFieldInfo out field info
     * @return n-ary mapping rule
     */
    NaryMappingRule<O> to(DslField<O> outFieldInfo);

}
