package io.doov.core.dsl.mapping;

import java.util.List;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.GenericTypeConverter;
import io.doov.core.dsl.lang.NaryStepMap;
import io.doov.core.dsl.lang.NaryStepMapping;

public class DefaultNaryStepMap implements NaryStepMap {

    private List<DslField> fieldInfos;

    public DefaultNaryStepMap(List<DslField> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    @Override
    public <O> NaryStepMapping<O> using(GenericTypeConverter<O> typeConverter) {
        return new DefaultNaryStepMapping<>(fieldInfos, typeConverter);
    }

}
