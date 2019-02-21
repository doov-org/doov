/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.HashMap;
import java.util.Map;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepWhen;

public class TemplatedRule extends DefaultValidationRule {

    private Map<FieldId,FieldId> resolutions;

    public TemplatedRule(StepWhen stepWhen) {
        super(stepWhen);
        this.resolutions = new HashMap<>();
    }

    public TemplatedRule bind(FieldId from, FieldId to) {
        this.resolutions.put(from,to);
        return this;
    }

    public <F,T extends FieldInfo & DslField<F>> TemplatedRule bind(T from, T to) {
        this.bind(from.id(),to.id());
        return this;
    }

    @Override
    public Result executeOn(DslModel model) {
        return super.executeOn(new TemplatedModel(model,resolutions));
    }

}
