/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.Function;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.StepCondition;

public class TemplateBuilder {
    private ParameterNamespace ns;
    private TemplatedRule template;

    public <T> TemplateBuilder(Function<ParameterNamespace,T> param1,Function<T, StepCondition> mktemplate) {
        this.ns = new ParameterNamespace();
        T param = param1.apply(ns);
        this.template  = new TemplatedRule(DOOV.when(mktemplate.apply(param)));
    }

    public <F,T extends FieldInfo & DslField<F>> TemplatedRule bind(Function<ParameterNamespace,T> from, T to) {
        template.bind(from.apply(ns),to);
        return this.template;
    }
}
