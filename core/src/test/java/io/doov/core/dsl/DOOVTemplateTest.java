/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl;

import static io.doov.core.dsl.template.ParameterNamespace.$Integer;

import org.junit.jupiter.api.Test;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.template.*;

public class DOOVTemplateTest {

    @Test
    void testParamCreation () {

        GenericModel model = new GenericModel();
        IntegerFieldInfo A = model.intField(3, "F1");
        StringFieldInfo B = model.stringField("", "F2");

        TemplateBuilder template= new TemplateBuilder($Integer("toto"), A::greaterOrEquals);

        System.out.println(template.bind($Integer("toto"),A).executeOn(model).value());

    }
}
