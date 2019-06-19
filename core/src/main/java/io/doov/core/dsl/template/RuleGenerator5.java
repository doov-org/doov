/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.DSLBuilder;
import io.doov.core.dsl.lang.Function5;
import io.doov.core.dsl.meta.Metadata;

public class RuleGenerator5<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
        T4 extends DslField<?>, T5 extends DslField<?>, R extends DSLBuilder> implements DSLBuilder {

    protected Function5<T1, T2, T3, T4, T5, R> ruleFunction;
    protected TemplateSpec.Template5<T1, T2, T3, T4, T5> template;

    public RuleGenerator5(Function5<T1, T2, T3, T4, T5, R> ruleFunction,
            TemplateSpec.Template5<T1, T2, T3, T4, T5> template) {
        this.ruleFunction = ruleFunction;
        this.template = template;
    }

    public R bind(T1 p1, T2 p2, T3 p3, T4 p4, T5 p5) {
        return this.ruleFunction.apply(
                template.param1.get().bind(p1).create(),
                template.param2.get().bind(p2).create(),
                template.param3.get().bind(p3).create(),
                template.param4.get().bind(p4).create(),
                template.param5.get().bind(p5).create());
    }

    @Override
    public Metadata metadata() {
        return bind(null, null, null, null, null).metadata();
    }
}
