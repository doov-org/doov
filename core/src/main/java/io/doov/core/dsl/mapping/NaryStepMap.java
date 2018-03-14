package io.doov.core.dsl.mapping;

import java.util.List;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.NaryTypeConverter;

/**
 * First step for creating a n-ary mapping rule.
 * Associates the in fields with with a generic converter
 */
public class NaryStepMap {

    private final List<DslField> fieldInfos;

    public NaryStepMap(List<DslField> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    /**
     * Return the step mapping
     *
     * @param typeConverter type converter
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> NaryStepMapping<O> using(NaryTypeConverter<O> typeConverter) {
        return new NaryStepMapping<>(fieldInfos, typeConverter);
    }

}
