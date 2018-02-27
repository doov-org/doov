package io.doov.core.dsl.mapping;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.SimpleMappingRule;
import io.doov.core.dsl.lang.StepMapping;
import io.doov.core.dsl.lang.TypeConverter;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultStepMapping<I, O> implements StepMapping<I, O> {

    private DslField<I> inFieldInfo;
    private TypeConverter<I, O> typeConverter;

    public DefaultStepMapping(DslField<I> inFieldInfo, TypeConverter<I, O> typeConverter) {
        this.inFieldInfo = inFieldInfo;
        this.typeConverter = typeConverter;
    }

    @Override
    public String readable() {
        return "to";
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        // TODO
    }

    @Override
    public SimpleMappingRule<I, O> to(DslField<O> outFieldInfo) {
        return new DefaultMappingRule<>(inFieldInfo, outFieldInfo, typeConverter);
    }
}
