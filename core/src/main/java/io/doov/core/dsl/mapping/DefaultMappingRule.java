package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.MappingRegistry;
import io.doov.core.dsl.lang.SimpleMappingRule;
import io.doov.core.dsl.lang.TypeConverter;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultMappingRule<I, O> implements SimpleMappingRule<I, O> {

    private DslField<I> inFieldInfo;
    private DslField<O> outFieldInfo;
    private TypeConverter<I, O> typeConverter;

    public DefaultMappingRule(DslField<I> inFieldInfo, DslField<O> outFieldInfo, TypeConverter<I, O> typeConverter) {
        this.inFieldInfo = inFieldInfo;
        this.outFieldInfo = outFieldInfo;
        this.typeConverter = typeConverter;
    }

    @Override
    public String readable() {
        return ".";
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        // TODO
    }

    @Override
    public boolean validate(FieldModel inModel, FieldModel outModel) {
        return inModel.getFieldInfos().stream().anyMatch(f -> f.id().equals(inFieldInfo.id()))
                && outModel.getFieldInfos().stream().anyMatch(f -> f.id().equals(outFieldInfo.id()));
    }

    @Override
    public void executeOn(FieldModel inModel, FieldModel outModel) {
        outModel.set(outFieldInfo.id(), typeConverter.convert(inModel, inFieldInfo));
    }

    @Override
    public SimpleMappingRule<I, O> registerOn(MappingRegistry registry) {
        registry.register(this);
        return this;
    }

}