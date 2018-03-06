package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.MappingMetadata.mapping;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.List;
import java.util.Locale;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.GenericTypeConverter;
import io.doov.core.dsl.lang.MappingRegistry;
import io.doov.core.dsl.lang.NaryMappingRule;
import io.doov.core.dsl.meta.*;

public class DefaultNaryMappingRule<O> implements NaryMappingRule<O> {

    private final MappingMetadata metadata;
    private final List<DslField> fieldInfos;
    private final DslField<O> outFieldInfo;
    private final GenericTypeConverter<O> typeConverter;

    public DefaultNaryMappingRule(List<DslField> fieldInfos, DslField<O> outFieldInfo,
                    GenericTypeConverter<O> typeConverter) {
        this.fieldInfos = fieldInfos;
        this.outFieldInfo = outFieldInfo;
        this.metadata = mapping(fieldInfos, outFieldInfo);
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
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
        typeConverter.accept(visitor, depth);
    }

    @Override
    public String readable() {
        return astToString(this, Locale.getDefault());
    }
}
