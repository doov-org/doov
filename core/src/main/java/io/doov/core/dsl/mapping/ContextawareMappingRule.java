/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.MappingMetadata.mapping;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.impl.ModelInterceptor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class ContextawareMappingRule<I, O> implements MappingRule {

    private final MappingMetadata metadata;
    private final BiFunction<DslModel, Context, I> valueFunction;
    private DslField<O> outFieldInfo;
    private StaticTypeConverter<I, O> converter;

    public ContextawareMappingRule(BiFunction<DslModel, Context, I> valueFunction, DslField<O> outFieldInfo,
            StaticTypeConverter<I, O> converter) {
        this.valueFunction = valueFunction;
        this.metadata = mapping(valueFunction, outFieldInfo);
        this.outFieldInfo = outFieldInfo;
        this.converter = converter;
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
        converter.accept(visitor, depth);
    }

    @Override
    public String readable(Locale locale) {
        return astToString(this, locale);
    }

    @Override
    public boolean validate(FieldModel inModel, FieldModel outModel) {
        return outModel.getFieldInfos().stream().anyMatch(f -> f.id().equals(outFieldInfo.id()));
    }

    @Override
    public <C extends Context> C executeOn(FieldModel inModel, FieldModel outModel, C context) {
        ModelInterceptor out = new ModelInterceptor(outModel, context);
        ModelInterceptor in = new ModelInterceptor(inModel, context);
        out.set(outFieldInfo.id(), converter.convert(context, valueFunction.apply(in, context)));
        return context;
    }

    @Override
    public Context executeOn(FieldModel inModel, FieldModel outModel) {
        return this.executeOn(inModel, outModel, new DefaultContext(metadata));
    }

}
