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

    protected Function3<T1, T2, T3, R> ruleFunction;
    protected TemplateSpec.Template3<T1, T2, T3> template;

    public RuleGenerator3(Function3<T1, T2, T3, R> ruleFunction, TemplateSpec.Template3<T1, T2, T3> template) {
        this.ruleFunction = ruleFunction;
        this.template = template;
    }

    public R bind(T1 p1, T2 p2, T3 p3) {
        return this.ruleFunction.apply(
                template.param1.get().bind(p1).create(),
                template.param2.get().bind(p2).create(),
                template.param3.get().bind(p3).create());
    }

    @Override
    public Metadata metadata() {
        return bind(null, null, null).metadata();
    }
}
