package io.doov.core.dsl.mapping;

import java.util.List;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.NaryTypeConverter;

/**
 * Intermediary step for creating n-ary mapping rule.
 * Associates in fields with the out field with type {@link O}
 *
 * @param <O> out type
 */
public class NaryStepMapping<O> {

    private final List<DslField> fieldInfos;
    private final NaryTypeConverter<O> typeConverter;

    NaryStepMapping(List<DslField> fieldInfos, NaryTypeConverter<O> typeConverter) {
        this.fieldInfos = fieldInfos;
        this.typeConverter = typeConverter;
    }

    /**
     * Return the n-ary mapping rule
     *
     * @param outFieldInfo out field info
     * @return n-ary mapping rule
     */
    public NaryMappingRule<O> to(DslField<O> outFieldInfo) {
        return new NaryMappingRule<>(fieldInfos, outFieldInfo, typeConverter);
    }

}
