package io.doov.core.dsl.mapping;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.TypeConverter;

/**
 * First step for creating mapping rule.
 * Associates the in field with type {@link I} with a converter or the out field with type {@link I}
 *
 * @param <I> in type
 */
public class SimpleStepMap<I> {

    private final DslField<I> inFieldInfo;

    public SimpleStepMap(DslField<I> inFieldInfo) {
        this.inFieldInfo = inFieldInfo;
    }

    /**
     * Return the step mapping
     *
     * @param typeConverter type converter
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> SimpleStepMapping<I, O> using(TypeConverter<I, O> typeConverter) {
        return new SimpleStepMapping<>(inFieldInfo, typeConverter);
    }

    /**
     * Return the mapping rule
     *
     * @param outFieldInfo out field info
     * @return the mapping rule
     */
    public SimpleMappingRule<I, I> to(DslField<I> outFieldInfo) {
        return new SimpleMappingRule<>(inFieldInfo, outFieldInfo, DefaultTypeConverter.identity());
    }
}
