package io.doov.core.dsl.mapping;

import java.util.List;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.GenericTypeConverter;
import io.doov.core.dsl.lang.NaryMappingRule;
import io.doov.core.dsl.lang.NaryStepMapping;

public class DefaultNaryStepMapping<O> implements NaryStepMapping<O> {

    private final List<DslField> fieldInfos;
    private final GenericTypeConverter<O> typeConverter;

    public DefaultNaryStepMapping(List<DslField> fieldInfos, GenericTypeConverter<O> typeConverter) {
        this.fieldInfos = fieldInfos;
        this.typeConverter = typeConverter;
    }

    @Override
    public NaryMappingRule<O> to(DslField<O> outFieldInfo) {
        return new DefaultNaryMappingRule<>(fieldInfos, outFieldInfo, typeConverter);
    }

}
