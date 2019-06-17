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

    private final Function5<T1, T2, T3, T4, T5, R> ruleFunction;
    private final TemplateSpec.Template5<T1, T2, T3, T4, T5> template;

    public RuleGenerator5(Function5<T1, T2, T3, T4, T5, R> ruleFunction,
            TemplateSpec.Template5<T1, T2, T3, T4, T5> template) {
        this.ruleFunction = ruleFunction;
        this.template = template;
    }

    public R bind(T1 p1, T2 p2, T3 p3, T4 p4, T5 p5) {
        TemplateParam<T1> param1 = template.param1.get();
        TemplateParam<T2> param2 = template.param2.get();
        TemplateParam<T3> param3 = template.param3.get();
        TemplateParam<T4> param4 = template.param4.get();
        TemplateParam<T5> param5 = template.param5.get();
        param1.bind(p1);
        param2.bind(p2);
        param3.bind(p3);
        param4.bind(p4);
        param5.bind(p5);
        return this.ruleFunction.apply(param1.create(), param2.create(),
                param3.create(), param4.create(), param5.create());
    }

    @Override
    public Metadata metadata() {
        return bind(null, null, null, null, null).metadata();
    }
}
