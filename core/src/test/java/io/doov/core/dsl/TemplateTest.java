/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl;

import static io.doov.core.dsl.template.ParameterNamespace.$Enum;
import static io.doov.core.dsl.template.ParameterNamespace.$Integer;

import java.time.DayOfWeek;
import java.time.Month;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.template.TemplateRule;
import io.doov.core.dsl.template.TemplateRule.Rule1;
import io.doov.core.dsl.template.TemplateRule.Rule2;

class TemplateTest {

    @Test
    void test1Param () {

        GenericModel model = new GenericModel();
        IntegerFieldInfo A = model.intField(3, "A");
        IntegerFieldInfo B = model.intField(5, "B");
        IntegerFieldInfo C = model.intField(1, "C");

        Rule1<Integer, IntegerFieldInfo> template = DOOV.template($Integer).with(
                p -> A.greaterThan(0).and(A.greaterThan(p))
        );

        Assertions.assertFalse(template.bind(B).executeOn(model).value());
        Assertions.assertTrue(template.bind(C).executeOn(model).value());

    }

    @Test
    void test2Params () {

        GenericModel model = new GenericModel();
        IntegerFieldInfo A = model.intField(3, "A");
        IntegerFieldInfo B = model.intField(5, "B");

        Rule2<Integer, IntegerFieldInfo, Integer, IntegerFieldInfo> template =
                DOOV.template($Integer,$Integer).with(NumericFieldInfo::greaterThan);

        Assertions.assertTrue(template.bind(B,A).executeOn(model).value());
        Assertions.assertFalse(template.bind(A,B).executeOn(model).value());

    }

    @Test
    void test3Params () {

        GenericModel model = new GenericModel();
        IntegerFieldInfo A = model.intField(3, "A");
        IntegerFieldInfo B = model.intField(5, "B");
        IntegerFieldInfo C = model.intField(5, "C");

        TemplateRule.Rule3<
                Integer, IntegerFieldInfo,
                Integer, IntegerFieldInfo,
                Integer, IntegerFieldInfo
                > template =
                DOOV.template($Integer,$Integer,$Integer).with(
                        (a,b,c) -> c.greaterThan(a).and(b.greaterThan(a))
                );

        Assertions.assertTrue(template.bind(A,B,C).executeOn(model).value());

    }

    @Test
    void testEnum () {

        GenericModel model = new GenericModel();
        EnumFieldInfo<Month> month = model.enumField(Month.DECEMBER,"");

        Rule1<Month, EnumFieldInfo<Month>> december =
                DOOV.template($Enum(Month.class)).with(present -> present.eq(Month.DECEMBER));

        Assertions.assertTrue(december.bind(month).executeOn(model).value());

    }
}
