/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.DSLBuilder;
import io.doov.core.dsl.lang.Function4;
import io.doov.core.dsl.lang.Function5;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.StepWhen;
import io.doov.core.dsl.lang.Function3;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;

public class TemplateRule {

    public static class Rule1<T1 extends DslField<?>> implements DSLBuilder {

        private final Function<T1, ValidationRule> ruleFunction;
        private final TemplateSpec.Template1<T1> template;
        private final ValidationRule validationRule;

        Rule1(Function<T1, StepCondition> ruleFunction, TemplateSpec.Template1<T1> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
            this.validationRule = this.ruleFunction.apply(template.param1.create());
        }

        public ValidationRule bind(T1 p1) {
            template.param1.bind(p1);
            return validationRule;
        }

        @Override
        public Metadata metadata() {
            return ruleFunction.apply(template.param1.create()).metadata();
        }
    }

    public static class Rule2<T1 extends DslField<?>, T2 extends DslField<?>> implements DSLBuilder {

        private final BiFunction<T1, T2, ValidationRule> ruleFunction;
        private final TemplateSpec.Template2<T1, T2> template;
        private final ValidationRule validationRule;

        Rule2(BiFunction<T1, T2, StepCondition> ruleFunction, TemplateSpec.Template2<T1, T2> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
            this.validationRule = this.ruleFunction.apply(template.param1.create(), template.param2.create());
        }

        public ValidationRule bind(T1 p1, T2 p2) {
            template.param1.bind(p1);
            template.param2.bind(p2);
            return validationRule;
        }

        @Override
        public Metadata metadata() {
            return validationRule.metadata();
        }

    }

    public static class Rule3<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>>
            implements DSLBuilder {

        private final Function3<T1, T2, T3, ValidationRule> ruleFunction;
        private final TemplateSpec.Template3<T1, T2, T3> template;
        private final ValidationRule validationRule;

        Rule3(Function3<T1, T2, T3, StepCondition> ruleFunction, TemplateSpec.Template3<T1, T2, T3> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
            this.validationRule = this.ruleFunction.apply(template.param1.create(), template.param2.create(),
                    template.param3.create());
        }

        public ValidationRule bind(T1 p1, T2 p2, T3 p3) {
            template.param1.bind(p1);
            template.param2.bind(p2);
            template.param3.bind(p3);
            return validationRule;
        }

        @Override
        public Metadata metadata() {
            return validationRule.metadata();
        }

    }

    public static class Rule4<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>, T4 extends DslField<?>>
            implements DSLBuilder {

        private final Function4<T1, T2, T3, T4, ValidationRule> ruleFunction;
        private final TemplateSpec.Template4<T1, T2, T3, T4> template;
        private final ValidationRule validationRule;

        Rule4(Function4<T1, T2, T3, T4, StepCondition> ruleFunction, TemplateSpec.Template4<T1, T2, T3, T4> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
            this.validationRule = this.ruleFunction.apply(template.param1.create(), template.param2.create(),
                    template.param3.create(), template.param4.create());
        }

        public ValidationRule bind(T1 p1, T2 p2, T3 p3, T4 p4) {
            template.param1.bind(p1);
            template.param2.bind(p2);
            template.param3.bind(p3);
            template.param4.bind(p4);
            return validationRule;
        }

        @Override
        public Metadata metadata() {
            return validationRule.metadata();
        }

    }

    public static class Rule5<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>, T4 extends DslField<?>, T5 extends DslField<?>>
            implements DSLBuilder {

        private final Function5<T1, T2, T3, T4, T5, ValidationRule> ruleFunction;
        private final TemplateSpec.Template5<T1, T2, T3, T4, T5> template;
        private final ValidationRule validationRule;

        Rule5(Function5<T1, T2, T3, T4, T5, StepCondition> ruleFunction,
                TemplateSpec.Template5<T1, T2, T3, T4, T5> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
            this.validationRule = this.ruleFunction.apply(template.param1.create(), template.param2.create(),
                    template.param3.create(), template.param4.create(), template.param5.create());
        }

        public ValidationRule bind(T1 p1, T2 p2, T3 p3, T4 p4, T5 p5) {
            template.param1.bind(p1);
            template.param2.bind(p2);
            template.param3.bind(p3);
            template.param4.bind(p4);
            template.param5.bind(p5);
            return validationRule;
        }

        @Override
        public Metadata metadata() {
            return validationRule.metadata();
        }

    }
}
