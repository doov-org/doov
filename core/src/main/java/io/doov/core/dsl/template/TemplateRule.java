/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.*;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.template.TemplateSpec.*;

public class TemplateRule {

    public static class Rule1<
            P1,T1 extends FieldInfo & DslField<P1>
            > {
        private ParameterNamespace ns;
        private Function<T1,StepCondition> ruleFunction;
        private Template1<P1,T1> template;

        Rule1(Template1<P1, T1> template, Function<T1, StepCondition> ruleFunction) {
            this.ns = new ParameterNamespace();
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public BoundRule bind(T1 dest) {
            T1 wildcard1 = template.param1.apply(ns,dest.readable());
            StepCondition condition = ruleFunction.apply(wildcard1);
            BoundRule boundedRule = new BoundRule(DOOV.when(condition));
            boundedRule.bind(wildcard1.id(),dest.id());
            return boundedRule;
        }

    }

    public static class Rule2<
            P1,T1 extends FieldInfo & DslField<P1>,
            P2,T2 extends FieldInfo & DslField<P2>
            > {
        private ParameterNamespace ns;
        private BiFunction<T1,T2,StepCondition> ruleFunction;
        private Template2<P1,T1,P2,T2> template;

        Rule2(Template2<P1,T1,P2,T2> template, BiFunction<T1,T2, StepCondition> ruleFunction) {
            this.ns = new ParameterNamespace();
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public BoundRule bind(T1 p1, T2 p2) {
            T1 wildcard1 = template.param1.apply(ns,p1.readable());
            T2 wildcard2 = template.param2.apply(ns,p2.readable());
            StepCondition condition = ruleFunction.apply(wildcard1,wildcard2);
            BoundRule boundedRule = new BoundRule(DOOV.when(condition));
            boundedRule.bind(wildcard1.id(),p1.id());
            boundedRule.bind(wildcard2.id(),p2.id());
            return boundedRule;
        }

    }

    public static class Rule3<
            P1,T1 extends FieldInfo & DslField<P1>,
            P2,T2 extends FieldInfo & DslField<P2>,
            P3,T3 extends FieldInfo & DslField<P3>
            > {
        private ParameterNamespace ns;
        private TriFunction<T1,T2,T3,StepCondition> ruleFunction;
        private Template3<P1,T1,P2,T2,P3,T3> template;

        Rule3(Template3<P1,T1,P2,T2,P3,T3> template, TriFunction<T1,T2,T3, StepCondition> ruleFunction) {
            this.ns = new ParameterNamespace();
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public BoundRule bind(T1 p1, T2 p2, T3 p3) {
            T1 wildcard1 = template.param1.apply(ns,p1.readable());
            T2 wildcard2 = template.param2.apply(ns,p2.readable());
            T3 wildcard3 = template.param3.apply(ns,p3.readable());
            StepCondition condition = ruleFunction.apply(wildcard1,wildcard2,wildcard3);
            BoundRule boundedRule = new BoundRule(DOOV.when(condition));
            boundedRule.bind(wildcard1.id(),p1.id());
            boundedRule.bind(wildcard2.id(),p2.id());
            boundedRule.bind(wildcard3.id(),p3.id());
            return boundedRule;
        }

    }
}
