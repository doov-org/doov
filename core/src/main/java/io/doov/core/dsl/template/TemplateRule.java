/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;

public class TemplateRule {

    public static class Rule1<T1 extends DslField<?>> implements DSLBuilder {

        private final Function<T1, ValidationRule> ruleFunction;
        private final TemplateSpec.Template1<T1> template;

        Rule1(Function<T1, StepCondition> ruleFunction, TemplateSpec.Template1<T1> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
        }

        public ValidationRule bind(T1 p1) {
            TemplateParam<T1> param1 = template.param1.get();
            param1.bind(p1);
            return this.ruleFunction.apply(param1.create());
        }

        @Override
        public Metadata metadata() {
            return ruleFunction.apply(template.param1.get().create()).metadata();
        }
    }

    public static class Rule2<T1 extends DslField<?>, T2 extends DslField<?>> implements DSLBuilder {

        private final BiFunction<T1, T2, ValidationRule> ruleFunction;
        private final TemplateSpec.Template2<T1, T2> template;

        Rule2(BiFunction<T1, T2, StepCondition> ruleFunction, TemplateSpec.Template2<T1, T2> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
        }

        public ValidationRule bind(T1 p1, T2 p2) {
            TemplateParam<T1> param1 = template.param1.get();
            TemplateParam<T2> param2 = template.param2.get();
            param1.bind(p1);
            param2.bind(p2);
            return this.ruleFunction.apply(param1.create(), param2.create());
        }

        @Override
        public Metadata metadata() {
            return this.ruleFunction.apply(template.param1.get().create(), template.param2.get().create()).metadata();
        }

    }

    public static class Rule3<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>>
            implements DSLBuilder {

        private final Function3<T1, T2, T3, ValidationRule> ruleFunction;
        private final TemplateSpec.Template3<T1, T2, T3> template;

        Rule3(Function3<T1, T2, T3, StepCondition> ruleFunction, TemplateSpec.Template3<T1, T2, T3> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
        }

        public ValidationRule bind(T1 p1, T2 p2, T3 p3) {

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
            return this.ruleFunction.apply(
                    template.param1.get().create(),
                    template.param2.get().create(),
                    template.param3.get().create())
                    .metadata();
        }

    }

    public static class Rule4<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>>
            implements DSLBuilder {

        private final Function4<T1, T2, T3, T4, ValidationRule> ruleFunction;
        private final TemplateSpec.Template4<T1, T2, T3, T4> template;

        Rule4(Function4<T1, T2, T3, T4, StepCondition> ruleFunction, TemplateSpec.Template4<T1, T2, T3, T4> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
        }

        public ValidationRule bind(T1 p1, T2 p2, T3 p3, T4 p4) {

            TemplateParam<T1> param1 = template.param1.get();
            TemplateParam<T2> param2 = template.param2.get();
            TemplateParam<T3> param3 = template.param3.get();
            TemplateParam<T4> param4 = template.param4.get();
            param1.bind(p1);
            param2.bind(p2);
            param3.bind(p3);
            param4.bind(p4);
            return this.ruleFunction.apply(param1.create(), param2.create(),
                    param3.create(), param4.create());
        }

        @Override
        public Metadata metadata() {
            return this.ruleFunction.apply(
                    template.param1.get().create(),
                    template.param2.get().create(),
                    template.param3.get().create(),
                    template.param4.get().create())
                    .metadata();
        }

    }

    public static class Rule5<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>, T5 extends DslField<?>>
            implements DSLBuilder {

        private final Function5<T1, T2, T3, T4, T5, ValidationRule> ruleFunction;
        private final TemplateSpec.Template5<T1, T2, T3, T4, T5> template;

        Rule5(Function5<T1, T2, T3, T4, T5, StepCondition> ruleFunction,
                TemplateSpec.Template5<T1, T2, T3, T4, T5> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
        }

        public ValidationRule bind(T1 p1, T2 p2, T3 p3, T4 p4, T5 p5) {

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
            return this.ruleFunction.apply(
                    template.param1.get().create(),
                    template.param2.get().create(),
                    template.param3.get().create(),
                    template.param4.get().create(),
                    template.param5.get().create())
                    .metadata();
        }

    }
}
