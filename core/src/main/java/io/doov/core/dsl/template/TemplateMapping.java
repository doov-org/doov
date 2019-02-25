/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;

public class TemplateMapping {

    public static class Map1<
            T1 extends FieldInfo & DslField<?>
            > {
        private Function<T1, MappingRule> ruleFunction;

        Map1(Function<T1, MappingRule> ruleFunction) {
            this.ruleFunction = ruleFunction;
        }

        public MappingRule bind(T1 p1) {
            return ruleFunction.apply(p1);
        }

    }

    public static class Map2<
            T1 extends FieldInfo & DslField<?>,
            T2 extends FieldInfo & DslField<?>
            > {
        private BiFunction<T1,T2,MappingRule> ruleFunction;

        Map2(BiFunction<T1,T2, MappingRule> ruleFunction) {
            this.ruleFunction = ruleFunction;
        }

        public MappingRule bind(T1 p1, T2 p2) {
            return ruleFunction.apply(p1,p2);
        }

    }

    public static class Map3<
            T1 extends FieldInfo & DslField<?>,
            T2 extends FieldInfo & DslField<?>,
            T3 extends FieldInfo & DslField<?>
            > {
        private TriFunction<T1,T2,T3,MappingRule> ruleFunction;

        Map3(TriFunction<T1,T2,T3, MappingRule> ruleFunction) {
            this.ruleFunction = ruleFunction;
        }

        public MappingRule bind(T1 p1, T2 p2, T3 p3) {
            return ruleFunction.apply(p1,p2,p3);
        }

    }
}
