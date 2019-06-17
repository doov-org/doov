/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.DSLBuilder;
import io.doov.core.dsl.meta.Metadata;

public class RuleGenerator2<T1 extends DslField<?>, T2 extends DslField<?>, R extends DSLBuilder>
        implements DSLBuilder {

    private final BiFunction<T1, T2, R> ruleFunction;
    private final TemplateSpec.Template2<T1, T2> template;

    public RuleGenerator2(BiFunction<T1, T2, R> ruleFunction, TemplateSpec.Template2<T1, T2> template) {
        this.ruleFunction = ruleFunction;
        this.template = template;
    }

    public R bind(T1 p1, T2 p2) {
        TemplateParam<T1> param1 = template.param1.get();
        TemplateParam<T2> param2 = template.param2.get();
        param1.bind(p1);
        param2.bind(p2);
        return this.ruleFunction.apply(param1.create(), param2.create());
    }

    @Override
    public Metadata metadata() {
        return bind(null, null).metadata();
    }
}
