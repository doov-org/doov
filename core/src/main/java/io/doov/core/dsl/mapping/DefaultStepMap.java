package io.doov.core.dsl.mapping;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.lang.SimpleMappingRule;
import io.doov.core.dsl.lang.StepMap;
import io.doov.core.dsl.lang.StepMapping;
import io.doov.core.dsl.lang.TypeConverter;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultStepMap<I> implements StepMap<I> {

    private DslField<I> inFieldInfo;

    public DefaultStepMap(DslField<I> inFieldInfo) {
        this.inFieldInfo = inFieldInfo;
    }

    @Override
    public <O> StepMapping<I, O> using(TypeConverter<I, O> typeConverter) {
        return new DefaultStepMapping<>(inFieldInfo, typeConverter);
    }

    @Override
    public SimpleMappingRule<I, I> to(DslField<I> outFieldInfo) {
        return new DefaultMappingRule<>(inFieldInfo, outFieldInfo, DefaultTypeConverter.identity());
    }
}
