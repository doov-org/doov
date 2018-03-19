package io.doov.core.dsl.mapping;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.TypeConverter;

/**
 * Intermediary step for creating mapping rule.
 * Associates in field with type {@link I} with the out field with type {@link O}
 *
 * @param <I> in type
 * @param <O> out type
 */
public class SimpleStepMapping<I, O> {

    private final DslField<I> inFieldInfo;
    private final TypeConverter<I, O> typeConverter;

    SimpleStepMapping(DslField<I> inFieldInfo, TypeConverter<I, O> typeConverter) {
        this.inFieldInfo = inFieldInfo;
        this.typeConverter = typeConverter;
    }

    /**
     * Return the mapping rule
     *
     * @param outFieldInfo out field info
     * @return simple mapping rule
     */
    public SimpleMappingRule<I, O> to(DslField<O> outFieldInfo) {
        return new SimpleMappingRule<>(inFieldInfo, outFieldInfo, typeConverter);
    }
}
