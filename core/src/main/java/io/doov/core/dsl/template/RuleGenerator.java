/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.DSLBuilder;
import io.doov.core.dsl.meta.Metadata;

public class RuleGenerator<T1 extends DslField<?>, R extends DSLBuilder> implements DSLBuilder {

    protected Function<T1, R> ruleFunction;
    protected TemplateSpec.Template1<T1> template;

    public RuleGenerator(Function<T1, R> ruleFunction, TemplateSpec.Template1<T1> template) {
        this.ruleFunction = ruleFunction;
        this.template = template;
    }

    public R bind(T1 p1) {
        return this.ruleFunction.apply(template.param1.get().bind(p1).create());
    }

    @Override
    public Metadata metadata() {
        return bind(null).metadata();
    }
}
