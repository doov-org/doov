/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.Map;

import io.doov.core.FieldId;
import io.doov.core.FieldModel;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.DefaultMappingRule;
import io.doov.core.dsl.meta.MappingRuleMetadata;

public class BoundMapping<T> extends DefaultMappingRule<T> {

    private final Map<FieldId, FieldId> resolutions;
    private final MappingInput<T> input;
    private final MappingOutput<T> output;
    private final MappingRuleMetadata metadata;

    public BoundMapping(
            MappingInput<T> input,
            MappingOutput<T> output,
            Map<FieldId, FieldId> resolutions,
            MappingRuleMetadata metadata
    ) {
        super(input, output);
        this.resolutions = resolutions;
        this.input = input;
        this.output = output;
        this.metadata = metadata;
    }

    void bind(FieldId from, FieldId to) {
        this.resolutions.put(from,to);
    }

    @Override
    public boolean validate(FieldModel inModel, FieldModel outModel) {
        TemplateModel in = new TemplateModel(inModel,resolutions);
        TemplateModel out = new TemplateModel(outModel,resolutions);
        return super.validate(in,out);
    }

    @Override
    public Context executeOn(FieldModel inModel, FieldModel outModel) {
        return super.executeOn(new TemplateModel(inModel,resolutions), outModel, new DefaultContext(metadata));
    }

    @Override
    public <C extends Context> C executeOn(FieldModel inModel, FieldModel outModel, C context) {
        return super.executeOn(new TemplateModel(inModel,resolutions), new TemplateModel(outModel,resolutions),context);
    }

}
