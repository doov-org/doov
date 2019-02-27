/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.runtime.GenericModel;

public class TemplateRule {

    public static class Rule1<T1 extends DslField<?>> implements Readable {

        private final Function<T1, ValidationRule> ruleFunction;
        private final TemplateSpec.Template1<T1> template;

        Rule1(Function<T1, StepCondition> ruleFunction, TemplateSpec.Template1<T1> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
        }

        public ValidationRule bind(T1 p1) {
            return ruleFunction.apply(p1);
        }

        @Override
        public String readable() {
            GenericModel mock = new GenericModel();
            return ruleFunction.apply(template.param1.generator.apply(mock)).readable();
        }
    }

    public static class Rule2<T1 extends DslField<?>, T2 extends DslField<?>> implements Readable {

        private final BiFunction<T1, T2, ValidationRule> ruleFunction;
        private final TemplateSpec.Template2<T1, T2> template;

        Rule2(BiFunction<T1, T2, StepCondition> ruleFunction, TemplateSpec.Template2<T1, T2> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
        }

        public ValidationRule bind(T1 p1, T2 p2) {
            return ruleFunction.apply(p1, p2);
        }

        @Override
        public String readable() {
            GenericModel mock = new GenericModel();
            return ruleFunction.apply(
                    template.param1.generator.apply(mock),
                    template.param2.generator.apply(mock)
            ).readable();
        }

    }

    public static class Rule3<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>>
            implements Readable {

        private final TriFunction<T1, T2, T3, ValidationRule> ruleFunction;
        private final TemplateSpec.Template3<T1, T2, T3> template;

        Rule3(TriFunction<T1, T2, T3, StepCondition> ruleFunction, TemplateSpec.Template3<T1, T2, T3> template) {
            this.ruleFunction = ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate);
            this.template = template;
        }

        public ValidationRule bind(T1 p1, T2 p2, T3 p3) {
            return ruleFunction.apply(p1, p2, p3);
        }

        @Override
        public String readable() {
            GenericModel mock = new GenericModel();
            return ruleFunction.apply(
                    template.param1.generator.apply(mock),
                    template.param2.generator.apply(mock),
                    template.param3.generator.apply(mock)
            ).readable();
        }

    }
}
