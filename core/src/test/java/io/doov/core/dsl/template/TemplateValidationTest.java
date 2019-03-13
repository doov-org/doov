/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import static io.doov.core.dsl.template.ParameterTypes.$Boolean;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.template.TemplateRule.Rule1;

class TemplateValidationTest {

    @Test
    void test1Param() {

        GenericModel model = new GenericModel();
        IntegerFieldInfo A = model.intField(3, "A");
        IntegerFieldInfo B = model.intField(9, "B");
        IntegerFieldInfo C = model.intField(1, "C");

        Rule1<IntegerFieldInfo> template = DOOV.template(ParameterTypes.$Integer).rule(
                A::greaterThan
        );

        System.out.println(template.readable());

        Assertions.assertFalse(template.bind(B).executeOn(model).value());
        Assertions.assertTrue(template.bind(C).executeOn(model).value());

    }

    @Test
    void test2Params() {

        GenericModel model = new GenericModel();
        IntegerFieldInfo A = model.intField(3, "A");
        IntegerFieldInfo B = model.intField(5, "B");

        TemplateRule.Rule2<IntegerFieldInfo, IntegerFieldInfo> template =
                DOOV.template(ParameterTypes.$Integer, ParameterTypes.$Integer).rule(NumericFieldInfo::greaterThan);

        Assertions.assertTrue(template.bind(B, A).executeOn(model).value());
        Assertions.assertFalse(template.bind(A, B).executeOn(model).value());

    }

    @Test
    void test3Params() {

        GenericModel model = new GenericModel();
        IntegerFieldInfo A = model.intField(3, "A");
        IntegerFieldInfo B = model.intField(5, "B");
        IntegerFieldInfo C = model.intField(5, "C");

        TemplateRule.Rule3<IntegerFieldInfo, IntegerFieldInfo, IntegerFieldInfo> template =
                DOOV.template(ParameterTypes.$Integer, ParameterTypes.$Integer, ParameterTypes.$Integer).with(
                        (a, b, c) -> c.greaterThan(a).and(b.greaterThan(a))
                );

        Assertions.assertTrue(template.bind(A, B, C).executeOn(model).value());

    }

    @Test
    void test3Params_different_types() {

        GenericModel model = new GenericModel();
        IntegerFieldInfo A = model.intField(3, "A");
        StringFieldInfo B = model.stringField("5", "B");
        BooleanFieldInfo C = model.booleanField(true, "C");

        TemplateRule.Rule3<IntegerFieldInfo, StringFieldInfo, BooleanFieldInfo> template =
                DOOV.template(ParameterTypes.$Integer, ParameterTypes.$String, $Boolean).with(
                        (a, b, c) -> c.isTrue().and(b.mapToInt(Integer::parseInt).greaterThan(a))
                );

        Assertions.assertTrue(template.bind(A, B, C).executeOn(model).value());

    }

    @Test
    void testEnum() {

        GenericModel model = new GenericModel();
        EnumFieldInfo<Month> month = model.enumField(Month.DECEMBER, "");

        Rule1<EnumFieldInfo<Month>> december =
                DOOV.template(ParameterTypes.$Enum(Month.class)).rule(present -> present.eq(Month.DECEMBER));

        Assertions.assertTrue(december.bind(month).executeOn(model).value());

    }

    @Test
    void testIterable() {

        GenericModel model = new GenericModel();

        List<Integer> ls1 = new ArrayList<>();

        ls1.add(1);
        ls1.add(2);
        ls1.add(3);

        IterableFieldInfo<Integer, Iterable<Integer>> with1 = model.iterableField(ls1, "list");

        List<Integer> ls2 = new ArrayList<>();

        ls2.add(2);
        ls2.add(3);

        IterableFieldInfo<Integer, Iterable<Integer>> without1 = model.iterableField(ls2, "list");

        Rule1<IterableFieldInfo<Integer, Iterable<Integer>>> has1 =
                DOOV.template(ParameterTypes.$Iterable(Integer.class)).rule(it -> it.contains(1));

        Assertions.assertTrue(has1.bind(with1).executeOn(model).value());
        Assertions.assertFalse(has1.bind(without1).executeOn(model).value());

    }

    @Test
    void testrebind_parameters() {
        GenericModel model = new GenericModel();
        BooleanFieldInfo _true = model.booleanField(true, "TrueField");
        BooleanFieldInfo _false = model.booleanField(false, "FalseField");

        Rule1<BooleanFieldInfo> templatedRule = DOOV.template($Boolean).rule(LogicalFieldInfo::isTrue);

        ValidationRule rule = templatedRule.bind(_true);
        Assertions.assertTrue(rule.executeOn(model).value());

        templatedRule.bind(_false);
        Assertions.assertFalse(rule.executeOn(model).value());

    }
}
