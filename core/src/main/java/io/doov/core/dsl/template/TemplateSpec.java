/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;

public class TemplateSpec {

    public static class Template1<T1 extends FieldInfo & DslField<?>> {

        final TemplateParam<T1> param1;

        public Template1(TemplateParam<T1> param1) {
            this.param1 = param1;
        }

        public TemplateRule.Rule1<T1> rule(Function<T1,StepCondition> ruleFunction) {
            return new TemplateRule.Rule1<>(ruleFunction, this);
        }

        public TemplateMapping.Map1<T1> mapping(Function<T1, MappingRule> ruleFunction) {
            return new TemplateMapping.Map1<>(ruleFunction, this);
        }
    }

    public static class Template2<
            T1 extends FieldInfo & DslField<?>,
            T2 extends FieldInfo & DslField<?>
            > {

        final TemplateParam<T1> param1;
        final TemplateParam<T2> param2;

        public Template2(
                TemplateParam<T1> param1,
                TemplateParam<T2> param2
        ) {
            this.param1 = param1;
            this.param2 = param2;
        }

        public TemplateRule.Rule2<T1, T2> rule(BiFunction<T1, T2,StepCondition> ruleFunction) {
            return new TemplateRule.Rule2<>(ruleFunction, this);
        }

        public TemplateMapping.Map2<T1, T2> mapping(BiFunction<T1, T2,MappingRule> ruleFunction) {
            return new TemplateMapping.Map2<>(ruleFunction, this);
        }
    }

    public static class Template3<
            T1 extends FieldInfo & DslField<?>,
            T2 extends FieldInfo & DslField<?>,
            T3 extends FieldInfo & DslField<?>
            > {

        final TemplateParam<T1> param1;
        final TemplateParam<T2> param2;
        final TemplateParam<T3> param3;

        public Template3(
                TemplateParam<T1> param1,
                TemplateParam<T2> param2,
                TemplateParam<T3> param3
        ) {
            this.param1 = param1;
            this.param2 = param2;
            this.param3 = param3;
        }

        public TemplateRule.Rule3<T1, T2, T3> with(TriFunction<T1, T2, T3, StepCondition> ruleFunction) {
            return new TemplateRule.Rule3<>(ruleFunction, this);
        }

        public TemplateMapping.Map3<T1, T2, T3> mapping(TriFunction<T1, T2, T3,MappingRule> ruleFunction) {
            return new TemplateMapping.Map3<>(ruleFunction, this);
        }
    }

}
