package io.doov.core.dsl.mapping;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.BiStepMap;
import io.doov.core.dsl.lang.BiStepMapping;
import io.doov.core.dsl.lang.BiTypeConverter;

public class DefaultBiStepMap<I, J> implements BiStepMap<I, J> {

    private DslField<I> inFieldInfo;
    private DslField<J> in2FieldInfo;

    public DefaultBiStepMap(DslField<I> inFieldInfo, DslField<J> in2FieldInfo) {
        this.inFieldInfo = inFieldInfo;
        this.in2FieldInfo = in2FieldInfo;
    }

    @Override
    public <O> BiStepMapping<I, J, O> using(BiTypeConverter<I, J, O> typeConverter) {
        return new DefaultBiStepMapping<>(inFieldInfo, in2FieldInfo, typeConverter);
    }

}
