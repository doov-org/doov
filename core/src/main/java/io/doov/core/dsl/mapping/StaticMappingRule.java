/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.function.Supplier;

import io.doov.core.FieldInfo;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MetadataVisitor;

public class StaticMappingRule<I, O> implements SimpleMappingRule<I, O> {

    private final Supplier<I> inputObject;
    private final DslField<O> outFieldInfo;
    private final StaticTypeConverter<I, O> typeConverter;

    public StaticMappingRule(Supplier<I> inputObject,
                             DslField<O> outFieldInfo,
                             StaticTypeConverter<I, O> typeConverter) {
        this.inputObject = inputObject;
        this.outFieldInfo = outFieldInfo;
        this.typeConverter = typeConverter;
    }

    @Override
    public boolean validate(FieldModel inModel, FieldModel outModel) {
        return outModel.getFieldInfos().stream().anyMatch(f -> f.id().equals(outFieldInfo.id()));
    }

    @Override
    public void executeOn(FieldModel inModel, FieldModel outModel) {
        outModel.set(outFieldInfo.id(), typeConverter.convert(inputObject.get()));
    }

    @Override
    public SimpleMappingRule<I, O> registerOn(MappingRegistry registry) {
        registry.register(this);
        return this;
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {

    }

    @Override
    public String readable() {
        return null;
    }

}
