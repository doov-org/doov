/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.MappingRegistry;
import io.doov.core.dsl.meta.Metadata;

public class TemplateMapping {

    public static class Map1<T1 extends DslField<?>> implements DSLBuilder {

        private Function<T1, MappingRule> ruleFunction;
        private final TemplateSpec.Template1<T1> template;

        Map1(Function<T1, MappingRule> ruleFunction, TemplateSpec.Template1<T1> template) {
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public MappingRule bind(T1 p1) {
            TemplateParam<T1> param1 = template.param1.get();
            param1.bind(p1);
            return ruleFunction.apply(param1.create());
        }

        @Override
        public Metadata metadata() {
            return this.ruleFunction.apply(template.param1.get().create()).metadata();
        }
    }

    public static class Map2<T1 extends DslField<?>, T2 extends DslField<?>> implements DSLBuilder {

        private BiFunction<T1, T2, MappingRule> ruleFunction;
        private final TemplateSpec.Template2<T1, T2> template;

        Map2(BiFunction<T1, T2, MappingRule> ruleFunction, TemplateSpec.Template2<T1, T2> template) {
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public MappingRule bind(T1 p1, T2 p2) {
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

    public static class Map3<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>>
            implements DSLBuilder {

        private Function3<T1, T2, T3, MappingRule> ruleFunction;
        private final TemplateSpec.Template3<T1, T2, T3> template;

        Map3(Function3<T1, T2, T3, MappingRule> ruleFunction, TemplateSpec.Template3<T1, T2, T3> template) {
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public MappingRule bind(T1 p1, T2 p2, T3 p3) {
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

    public static class Map4<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>>
            implements DSLBuilder {

        private Function4<T1, T2, T3, T4, MappingRule> ruleFunction;
        private final TemplateSpec.Template4<T1, T2, T3, T4> template;

        Map4(Function4<T1, T2, T3, T4, MappingRule> ruleFunction, TemplateSpec.Template4<T1, T2, T3, T4> template) {
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public MappingRule bind(T1 p1, T2 p2, T3 p3, T4 p4) {
            TemplateParam<T1> param1 = template.param1.get();
            TemplateParam<T2> param2 = template.param2.get();
            TemplateParam<T3> param3 = template.param3.get();
            TemplateParam<T4> param4 = template.param4.get();
            ;
            param1.bind(p1);
            param2.bind(p2);
            param3.bind(p3);
            param4.bind(p4);
            return this.ruleFunction.apply(
                    param1.create(),
                    param2.create(),
                    param3.create(),
                    param4.create());
        }

        @Override
        public Metadata metadata() {
            return this.ruleFunction.apply(
                    template.param1.get().create(),
                    template.param2.get().create(),
                    template.param3.get().create(),
                    template.param4.get().create()).metadata();
        }
    }

    public static class Map5<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>, T5 extends DslField<?>>
            implements DSLBuilder {

        private Function5<T1, T2, T3, T4, T5, MappingRule> ruleFunction;
        private final TemplateSpec.Template5<T1, T2, T3, T4, T5> template;

        Map5(Function5<T1, T2, T3, T4, T5, MappingRule> ruleFunction,
                TemplateSpec.Template5<T1, T2, T3, T4, T5> template) {
            this.ruleFunction = ruleFunction;
            this.template = template;
        }

        public MappingRule bind(T1 p1, T2 p2, T3 p3, T4 p4, T5 p5) {

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
            return this.ruleFunction.apply(
                    param1.create(),
                    param2.create(),
                    param3.create(),
                    param4.create(),
                    param5.create());
        }

        @Override
        public Metadata metadata() {
            return this.ruleFunction.apply(
                    template.param1.get().create(),
                    template.param2.get().create(),
                    template.param3.get().create(),
                    template.param4.get().create(),
                    template.param5.get().create()).metadata();
        }
    }

    public static class Registry1<T1 extends DslField<?>> implements DSLBuilder {

        private Function<T1, MappingRegistry> registryFunction;
        private final TemplateSpec.Template1<T1> template;

        Registry1(Function<T1, MappingRegistry> registryFunction, TemplateSpec.Template1<T1> template) {
            this.registryFunction = registryFunction;
            this.template = template;
        }

        public MappingRegistry bind(T1 p1) {
            TemplateParam<T1> param1 = template.param1.get();
            param1.bind(p1);
            return this.registryFunction.apply(param1.create());
        }

        @Override
        public Metadata metadata() {
            return registryFunction.apply(template.param1.get().create()).metadata();
        }
    }

    public static class Registry2<T1 extends DslField<?>, T2 extends DslField<?>> implements DSLBuilder {

        private BiFunction<T1, T2, MappingRegistry> registryFunction;
        private final TemplateSpec.Template2<T1, T2> template;

        Registry2(BiFunction<T1, T2, MappingRegistry> registryFunction, TemplateSpec.Template2<T1, T2> template) {
            this.registryFunction = registryFunction;
            this.template = template;
        }

        public MappingRegistry bind(T1 p1, T2 p2) {
            TemplateParam<T1> param1 = template.param1.get();
            TemplateParam<T2> param2 = template.param2.get();
            param1.bind(p1);
            param2.bind(p2);
            return this.registryFunction.apply(param1.create(), param2.create());
        }

        @Override
        public Metadata metadata() {
            return this.registryFunction.apply(template.param1.get().create(), template.param2.get().create()).metadata();
        }
    }

    public static class Registry3<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>>
            implements DSLBuilder {

        private Function3<T1, T2, T3, MappingRegistry> registryFunction;
        private final TemplateSpec.Template3<T1, T2, T3> template;

        Registry3(
                Function3<T1, T2, T3, MappingRegistry> registryFunction,
                TemplateSpec.Template3<T1, T2, T3> template) {
            this.registryFunction = registryFunction;
            this.template = template;
        }

        public MappingRegistry bind(T1 p1, T2 p2, T3 p3) {
            TemplateParam<T1> param1 = template.param1.get();
            TemplateParam<T2> param2 = template.param2.get();
            TemplateParam<T3> param3 = template.param3.get();
            param1.bind(p1);
            param2.bind(p2);
            param3.bind(p3);
            return this.registryFunction.apply(
                    param1.create(),
                    param2.create(),
                    param3.create());
        }

        @Override
        public Metadata metadata() {
            return this.registryFunction.apply(
                    template.param1.get().create(),
                    template.param2.get().create(),
                    template.param3.get().create()).metadata();
        }
    }

    public static class Registry4<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>>
            implements DSLBuilder {

        private Function4<T1, T2, T3, T4, MappingRegistry> registryFunction;
        private final TemplateSpec.Template4<T1, T2, T3, T4> template;

        Registry4(Function4<T1, T2, T3, T4, MappingRegistry> registryFunction,
                TemplateSpec.Template4<T1, T2, T3, T4> template) {
            this.registryFunction = registryFunction;
            this.template = template;
        }

        public MappingRegistry bind(T1 p1, T2 p2, T3 p3, T4 p4) {
            TemplateParam<T1> param1 = template.param1.get();
            TemplateParam<T2> param2 = template.param2.get();
            TemplateParam<T3> param3 = template.param3.get();
            TemplateParam<T4> param4 = template.param4.get();
            param1.bind(p1);
            param2.bind(p2);
            param3.bind(p3);
            param4.bind(p4);
            return registryFunction.apply(
                    param1.create(),
                    param2.create(),
                    param3.create(),
                    param4.create());
        }

        @Override
        public Metadata metadata() {
            return this.registryFunction.apply(
                    template.param1.get().create(),
                    template.param2.get().create(),
                    template.param3.get().create(),
                    template.param4.get().create()).metadata();
        }
    }

    public static class Registry5<T1 extends DslField<?>, T2 extends DslField<?>, T3 extends DslField<?>,
            T4 extends DslField<?>, T5 extends DslField<?>>
            implements DSLBuilder {

        private Function5<T1, T2, T3, T4, T5, MappingRegistry> registryFunction;
        private final TemplateSpec.Template5<T1, T2, T3, T4, T5> template;

        Registry5(Function5<T1, T2, T3, T4, T5, MappingRegistry> registryFunction,
                TemplateSpec.Template5<T1, T2, T3, T4, T5> template) {
            this.registryFunction = registryFunction;
            this.template = template;
        }

        public MappingRegistry bind(T1 p1, T2 p2, T3 p3, T4 p4, T5 p5) {
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
            return this.registryFunction.apply(
                    param1.create(),
                    param2.create(),
                    param3.create(),
                    param4.create(),
                    param5.create());
        }

        @Override
        public Metadata metadata() {
            return this.registryFunction.apply(
                    template.param1.get().create(),
                    template.param2.get().create(),
                    template.param3.get().create(),
                    template.param4.get().create(),
                    template.param5.get().create()).metadata();
        }
    }
}
