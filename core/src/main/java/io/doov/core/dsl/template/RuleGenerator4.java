/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.DSLBuilder;
import io.doov.core.dsl.lang.Function4;
import io.doov.core.dsl.meta.Metadata;

public class RuleGenerator4<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
        T4 extends DslField<?>, R extends DSLBuilder> implements DSLBuilder {

    protected Function4<T1, T2, T3, T4, R> ruleFunction;
    protected TemplateSpec.Template4<T1, T2, T3, T4> template;

    public RuleGenerator4(Function4<T1, T2, T3, T4, R> ruleFunction, TemplateSpec.Template4<T1, T2, T3, T4> template) {
        this.ruleFunction = ruleFunction;
        this.template = template;
    }

    public R bind(T1 p1, T2 p2, T3 p3, T4 p4) {
        return this.ruleFunction.apply(
                template.param1.get().bind(p1).create(),
                template.param2.get().bind(p2).create(),
                template.param3.get().bind(p3).create(),
                template.param4.get().bind(p4).create());
    }

    @Override
    public Metadata metadata() {
        return bind(null, null, null, null).metadata();
    }
}
