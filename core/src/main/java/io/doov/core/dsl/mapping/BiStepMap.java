package io.doov.core.dsl.mapping;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.BiTypeConverter;

/**
 * First step for creating mapping rule.
 * Associates field with type {@link I} and {@link J} with a bi-converter.
 *
 * @param <I> 1st in type
 * @param <J> 2nd in type
 */
public class BiStepMap<I, J> {

    private final DslField<I> inFieldInfo;
    private final DslField<J> in2FieldInfo;

    public BiStepMap(DslField<I> inFieldInfo, DslField<J> in2FieldInfo) {
        this.inFieldInfo = inFieldInfo;
        this.in2FieldInfo = in2FieldInfo;
    }

    /**
     * Returns bi step mapping
     *
     * @param typeConverter bi type converter
     * @param <O>           out type
     * @return bi step mapping
     */
    public <O> BiStepMapping<I, J, O> using(BiTypeConverter<I, J, O> typeConverter) {
        return new BiStepMapping<>(inFieldInfo, in2FieldInfo, typeConverter);
    }

}
