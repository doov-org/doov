/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.TriFunction;
import io.doov.core.dsl.template.ParameterNamespace.ParamProvider;

public class TemplateSpec {

    public static class Template1<P1,T1 extends FieldInfo & DslField<P1>> {
        final ParamProvider<P1,T1> param1;

        public Template1(
                ParamProvider<P1,T1> param1
        ) {
            this.param1 = param1;
        }

        public TemplateRule.Rule1<P1,T1> with(Function<T1, StepCondition> ruleFunction) {
            return new TemplateRule.Rule1<>(this,ruleFunction);
        }
    }

    public static class Template2<
            P1,T1 extends FieldInfo & DslField<P1>,
            P2,T2 extends FieldInfo & DslField<P2>
            > {
        final ParamProvider<P1,T1> param1;
        final ParamProvider<P2,T2> param2;

        public Template2(
                ParamProvider<P1, T1> param1,
                ParamProvider<P2, T2> param2
        ) {
            this.param1 = param1;
            this.param2 = param2;
        }

        public TemplateRule.Rule2<P1,T1,P2,T2> with(BiFunction<T1,T2, StepCondition> ruleFunction) {
            return new TemplateRule.Rule2<>(this,ruleFunction);
        }
    }

    public static class Template3<
            P1,T1 extends FieldInfo & DslField<P1>,
            P2,T2 extends FieldInfo & DslField<P2>,
            P3,T3 extends FieldInfo & DslField<P3>
            > {
        final ParamProvider<P1,T1> param1;
        final ParamProvider<P2,T2> param2;
        final ParamProvider<P3,T3> param3;

        public Template3(
                ParamProvider<P1, T1> param1,
                ParamProvider<P2, T2> param2,
                ParamProvider<P3, T3> param3
        ) {
            this.param1 = param1;
            this.param2 = param2;
            this.param3 = param3;
        }

        public TemplateRule.Rule3<P1,T1,P2,T2,P3,T3> with(TriFunction<T1,T2,T3, StepCondition> ruleFunction) {
            return new TemplateRule.Rule3<>(this,ruleFunction);
        }
    }

}
