package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.MappingMetadata.mapping;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.List;
import java.util.Locale;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.impl.ModelInterceptor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

/**
 * n-to-1 mapping rule
 *
 * @param <O> out type
 */
public class NaryMappingRule<O> implements MappingRule {

    private final MappingMetadata metadata;
    private final List<DslField> fieldInfos;
    private final DslField<O> outFieldInfo;
    private final NaryTypeConverter<O> typeConverter;

    NaryMappingRule(List<DslField> fieldInfos, DslField<O> outFieldInfo,
            NaryTypeConverter<O> typeConverter) {
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
    public <C extends Context> C executeOn(FieldModel inModel, FieldModel outModel, C context) {
        ModelInterceptor in = new ModelInterceptor(inModel, context);
        ModelInterceptor out = new ModelInterceptor(outModel, context);
        out.set(outFieldInfo.id(), typeConverter.convert(in, context, fieldInfos.toArray(new DslField[0])));
        return context;
    }

    @Override
    public Context executeOn(FieldModel inModel, FieldModel outModel) {
        return this.executeOn(inModel, outModel, new DefaultContext(metadata));
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
        typeConverter.accept(visitor, depth);
    }

    @Override
    public String readable(Locale locale) {
        return astToString(this, locale);
    }
}
