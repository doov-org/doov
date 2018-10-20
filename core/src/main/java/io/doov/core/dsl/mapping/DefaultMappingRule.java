/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.impl.ModelInterceptor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class DefaultMappingRule<T> extends AbstractDSLBuilder implements MappingRule {

    private final MappingInput<T> input;
    private final MappingOutput<T> output;
    private final MappingRuleMetadata metadata;

    public DefaultMappingRule(MappingInput<T> input, MappingOutput<T> output) {
        this.input = input;
        this.output = output;
        this.metadata = new MappingRuleMetadata(input.metadata(), output.metadata());
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public boolean validate(FieldModel inModel, FieldModel outModel) {
        return input.validate(inModel) && output.validate(outModel);
    }

    @Override
    public Context executeOn(FieldModel inModel, FieldModel outModel) {
        return executeOn(inModel, outModel, new DefaultContext(metadata));
    }

    @Override
    public <C extends Context> C executeOn(FieldModel inModel, FieldModel outModel, C context) {
        ModelInterceptor in = new ModelInterceptor(inModel, context);
        ModelInterceptor out = new ModelInterceptor(outModel, context);
        output.write(out, context, input.read(in, context));
        return context;
    }

}
