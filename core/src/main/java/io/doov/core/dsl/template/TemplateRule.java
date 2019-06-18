/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;

public class TemplateRule {

    public static class Rule1<T1 extends DslField<?>> extends RuleGenerator<T1, ValidationRule> {

        Rule1(Function<T1, StepCondition> ruleFunction, TemplateSpec.Template1<T1> template) {
            super(ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate), template);
        }
    }

    public static class Rule2<T1 extends DslField<?>, T2 extends DslField<?>>
            extends RuleGenerator2<T1, T2, ValidationRule> {

        Rule2(BiFunction<T1, T2, StepCondition> ruleFunction, TemplateSpec.Template2<T1, T2> template) {
            super(ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate), template);
        }
    }

    public static class Rule3<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>>
            extends RuleGenerator3<T1, T2, T3, ValidationRule> {

        Rule3(Function3<T1, T2, T3, StepCondition> ruleFunction, TemplateSpec.Template3<T1, T2, T3> template) {
            super(ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate), template);
        }
    }

    public static class Rule4<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>>
            extends RuleGenerator4<T1, T2, T3, T4, ValidationRule> {

        Rule4(Function4<T1, T2, T3, T4, StepCondition> ruleFunction, TemplateSpec.Template4<T1, T2, T3, T4> template) {
            super(ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate), template);
        }

    }

    public static class Rule5<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>, T5 extends DslField<?>>
            extends RuleGenerator5<T1, T2, T3, T4, T5, ValidationRule> {

        Rule5(Function5<T1, T2, T3, T4, T5, StepCondition> ruleFunction,
                TemplateSpec.Template5<T1, T2, T3, T4, T5> template) {
            super(ruleFunction.andThen(DOOV::when).andThen(StepWhen::validate), template);
        }
    }
}
