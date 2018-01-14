package io.doov.core.dsl.mapping;

import java.util.List;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.GenericTypeConverter;
import io.doov.core.dsl.lang.MappingRegistry;
import io.doov.core.dsl.lang.NaryMappingRule;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultNaryMappingRule<O> implements NaryMappingRule<O> {

    private List<DslField> fieldInfos;
    private DslField<O> outFieldInfo;
    private GenericTypeConverter<O> typeConverter;

    public DefaultNaryMappingRule(List<DslField> fieldInfos, DslField<O> outFieldInfo,
                                  GenericTypeConverter<O> typeConverter) {
        this.fieldInfos = fieldInfos;
        this.outFieldInfo = outFieldInfo;
        this.typeConverter = typeConverter;
    }

    @Override
    public boolean validate(FieldModel inModel, FieldModel outModel) {
        return fieldInfos.stream().allMatch(f -> inModel.getFieldInfos().stream().anyMatch(i -> i.id().equals(f.id())))
                && outModel.getFieldInfos().stream().anyMatch(f -> f.id().equals(outFieldInfo.id()));
    }

    @Override
    public void executeOn(FieldModel inModel, FieldModel outModel) {
        outModel.set(outFieldInfo.id(),
                typeConverter.convert(inModel, fieldInfos.toArray(new DslField[0])));

    }

    @Override
    public NaryMappingRule<O> registerOn(MappingRegistry registry) {
        registry.register(this);
        return this;
    }

    @Override
    public String readable() {
        return null;
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {

    }
}
