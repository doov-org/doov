/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.HashMap;
import java.util.Map;

import io.doov.core.FieldId;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.impl.DefaultValidationRule;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepWhen;

public class BoundRule extends DefaultValidationRule {

    private final Map<FieldId,FieldId> resolutions;

    BoundRule(StepWhen stepWhen) {
        super(stepWhen);
        this.resolutions = new HashMap<>();
    }

    void bind(FieldId from, FieldId to) {
        this.resolutions.put(from,to);
    }

    @Override
    public Result executeOn(DslModel model) {
        return super.executeOn(new TemplateModel(model,resolutions));
    }

}
