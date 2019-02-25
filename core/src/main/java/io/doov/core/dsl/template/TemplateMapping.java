/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.lang.TriFunction;

public class TemplateMapping {

    public static class Map1<
            T1 extends FieldInfo & DslField<?>
            > {

        private final Function<T1, MappingRule> ruleFunction;
        private final TemplateSpec.Template1<T1> template;

        Map1(Function<T1, MappingRule> ruleFunction, TemplateSpec.Template1<T1> template) {
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public MappingRule bind(T1 p1) {
            return ruleFunction.apply(p1);
        }

    }

    public static class Map2<
            T1 extends FieldInfo & DslField<?>,
            T2 extends FieldInfo & DslField<?>
            > {

        private final BiFunction<T1, T2, MappingRule> ruleFunction;
        private final TemplateSpec.Template2<T1, T2> template;

        Map2(BiFunction<T1, T2, MappingRule> ruleFunction, TemplateSpec.Template2<T1, T2> template) {
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public MappingRule bind(T1 p1, T2 p2) {
            return ruleFunction.apply(p1, p2);
        }

    }

    public static class Map3<
            T1 extends FieldInfo & DslField<?>,
            T2 extends FieldInfo & DslField<?>,
            T3 extends FieldInfo & DslField<?>
            > {

        private final TriFunction<T1, T2, T3, MappingRule> ruleFunction;
        private final TemplateSpec.Template3<T1, T2, T3> template;

        Map3(TriFunction<T1, T2, T3, MappingRule> ruleFunction, TemplateSpec.Template3<T1, T2, T3> template) {
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public MappingRule bind(T1 p1, T2 p2, T3 p3) {
            return ruleFunction.apply(p1, p2, p3);
        }

    }
}
