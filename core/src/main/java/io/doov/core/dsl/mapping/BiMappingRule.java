package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.MappingMetadata.mapping;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Arrays;
import java.util.Locale;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.impl.ModelInterceptor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

/**
 * 2-to-1 mapping rule
 *
 * @param <I> 1st in type
 * @param <J> 2nd in type
 * @param <O> out type
 */
public class BiMappingRule<I, J, O> implements MappingRule {

    private final DslField<I> inFieldInfo;
    private final DslField<J> in2FieldInfo;
    private final MappingMetadata metadata;
    private final DslField<O> outFieldInfo;
    private final BiTypeConverter<I, J, O> typeConverter;

    BiMappingRule(DslField<I> inFieldInfo, DslField<J> in2FieldInfo,
            DslField<O> outFieldInfo, BiTypeConverter<I, J, O> typeConverter) {
        this.inFieldInfo = inFieldInfo;
        this.in2FieldInfo = in2FieldInfo;
        this.metadata = mapping(Arrays.asList(inFieldInfo, in2FieldInfo), outFieldInfo);
        this.outFieldInfo = outFieldInfo;
        this.typeConverter = typeConverter;
    }

    @Override
    public boolean validate(FieldModel inModel, FieldModel outModel) {
        return inModel.getFieldInfos().stream().anyMatch(f -> f.id().equals(inFieldInfo.id()))
                && inModel.getFieldInfos().stream().anyMatch(f -> f.id().equals(in2FieldInfo.id()))
                && outModel.getFieldInfos().stream().anyMatch(f -> f.id().equals(outFieldInfo.id()));
    }

    @Override
    public <C extends Context> C executeOn(FieldModel inModel, FieldModel outModel, C context) {
        ModelInterceptor in = new ModelInterceptor(inModel, context);
        ModelInterceptor out = new ModelInterceptor(outModel, context);
        out.set(outFieldInfo.id(), typeConverter.convert(in, context, inFieldInfo, in2FieldInfo));
        return context;
    }

    @Override
    public Context executeOn(FieldModel inModel, FieldModel outModel) {
        return executeOn(inModel, outModel, new DefaultContext(this.metadata));
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
