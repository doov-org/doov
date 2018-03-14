package io.doov.core.dsl.mapping;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.BiTypeConverter;

/**
 * Intermediary step for creating a bi mapping rule.
 * Associates in fields with type {@link I} and {@link J} with the out field with type {@link O}
 *
 * @param <I> 1st in type
 * @param <J> 2nd in type
 * @param <O> out type
 */
public class BiStepMapping<I, J, O> {

    private final DslField<I> inFieldInfo;
    private final DslField<J> in2FieldInfo;
    private final BiTypeConverter<I, J, O> typeConverter;

    BiStepMapping(DslField<I> inFieldInfo, DslField<J> in2FieldInfo,
            BiTypeConverter<I, J, O> typeConverter) {

        this.inFieldInfo = inFieldInfo;
        this.in2FieldInfo = in2FieldInfo;
        this.typeConverter = typeConverter;
    }

    /**
     * Return the bi mapping rule
     *
     * @param outFieldInfo out field
     * @return bi mapping rule
     */
    public BiMappingRule<I, J, O> to(DslField<O> outFieldInfo) {
        return new BiMappingRule<>(inFieldInfo, in2FieldInfo, outFieldInfo, typeConverter);
    }

}
