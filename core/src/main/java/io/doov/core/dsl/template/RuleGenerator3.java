/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.DSLBuilder;
import io.doov.core.dsl.lang.Function3;
import io.doov.core.dsl.meta.Metadata;

public class RuleGenerator3<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
        R extends DSLBuilder> implements DSLBuilder {

    private final Function3<T1, T2, T3, R> ruleFunction;
    private final TemplateSpec.Template3<T1, T2, T3> template;

    public RuleGenerator3(Function3<T1, T2, T3, R> ruleFunction, TemplateSpec.Template3<T1, T2, T3> template) {
        this.ruleFunction = ruleFunction;
        this.template = template;
    }

    public R bind(T1 p1, T2 p2, T3 p3) {
        TemplateParam<T1> param1 = template.param1.get();
        TemplateParam<T2> param2 = template.param2.get();
        TemplateParam<T3> param3 = template.param3.get();
        param1.bind(p1);
        param2.bind(p2);
        param3.bind(p3);
        return this.ruleFunction.apply(param1.create(), param2.create(), param3.create());
    }

    @Override
    public Metadata metadata() {
        return bind(null, null, null).metadata();
    }
}
