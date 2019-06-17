/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.MappingRegistry;

public class TemplateMapping {

    public static class Map1<T1 extends DslField<?>> extends RuleGenerator<T1, MappingRule> {

        Map1(Function<T1, MappingRule> ruleFunction, TemplateSpec.Template1<T1> template) {
            super(ruleFunction, template);
        }
    }

    public static class Map2<T1 extends DslField<?>, T2 extends DslField<?>>
            extends RuleGenerator2<T1, T2, MappingRule> {

        Map2(BiFunction<T1, T2, MappingRule> ruleFunction, TemplateSpec.Template2<T1, T2> template) {
            super(ruleFunction, template);
        }

    }

    public static class Map3<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>>
            extends RuleGenerator3<T1, T2, T3, MappingRule> {

        Map3(Function3<T1, T2, T3, MappingRule> ruleFunction, TemplateSpec.Template3<T1, T2, T3> template) {
            super(ruleFunction, template);
        }
    }

    public static class Map4<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>>
            extends RuleGenerator4<T1, T2, T3, T4, MappingRule> {

        Map4(Function4<T1, T2, T3, T4, MappingRule> ruleFunction, TemplateSpec.Template4<T1, T2, T3, T4> template) {
            super(ruleFunction, template);
        }
    }

    public static class Map5<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>, T5 extends DslField<?>>
            extends RuleGenerator5<T1, T2, T3, T4, T5, MappingRule> {

        Map5(Function5<T1, T2, T3, T4, T5, MappingRule> ruleFunction,
                TemplateSpec.Template5<T1, T2, T3, T4, T5> template) {
            super(ruleFunction, template);
        }
    }

    public static class Registry1<T1 extends DslField<?>> extends RuleGenerator<T1, MappingRegistry> {

        Registry1(Function<T1, MappingRegistry> registryFunction, TemplateSpec.Template1<T1> template) {
            super(registryFunction, template);
        }
    }

    public static class Registry2<T1 extends DslField<?>, T2 extends DslField<?>>
            extends RuleGenerator2<T1, T2, MappingRegistry> {

        Registry2(BiFunction<T1, T2, MappingRegistry> registryFunction, TemplateSpec.Template2<T1, T2> template) {
            super(registryFunction, template);
        }
    }

    public static class Registry3<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>>
            extends RuleGenerator3<T1, T2, T3, MappingRegistry> {

        Registry3(Function3<T1, T2, T3, MappingRegistry> registryFunction,
                TemplateSpec.Template3<T1, T2, T3> template) {
            super(registryFunction, template);
        }
    }

    public static class Registry4<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>>
            extends RuleGenerator4<T1, T2, T3, T4, MappingRegistry> {

        Registry4(Function4<T1, T2, T3, T4, MappingRegistry> registryFunction,
                TemplateSpec.Template4<T1, T2, T3, T4> template) {
            super(registryFunction, template);
        }
    }

    public static class Registry5<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>, T5 extends DslField<?>>
            extends RuleGenerator5<T1, T2, T3, T4, T5, MappingRegistry> {

        Registry5(Function5<T1, T2, T3, T4, T5, MappingRegistry> registryFunction,
                TemplateSpec.Template5<T1, T2, T3, T4, T5> template) {
            super(registryFunction, template);
        }
    }
}
