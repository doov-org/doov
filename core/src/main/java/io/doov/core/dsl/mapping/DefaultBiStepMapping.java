package io.doov.core.dsl.mapping;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.BiMappingRule;
import io.doov.core.dsl.lang.BiStepMapping;
import io.doov.core.dsl.lang.BiTypeConverter;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultBiStepMapping<I, J, O> implements BiStepMapping<I, J, O> {
    private final DslField<I> inFieldInfo;
    private final DslField<J> in2FieldInfo;
    private final BiTypeConverter<I, J, O> typeConverter;

    public DefaultBiStepMapping(DslField<I> inFieldInfo, DslField<J> in2FieldInfo,
                                BiTypeConverter<I, J, O> typeConverter) {

        this.inFieldInfo = inFieldInfo;
        this.in2FieldInfo = in2FieldInfo;
        this.typeConverter = typeConverter;
    }

    @Override
    public BiMappingRule<I, J, O> to(DslField<O> outFieldInfo) {
        return new DefaultBiMappingRule<>(inFieldInfo, in2FieldInfo, outFieldInfo, typeConverter);
    }

    @Override
    public String readable() {
        return null;
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        // TODO
    }
}
