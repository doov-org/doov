/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping.builder;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.mapping.TypeConverters;
import io.doov.core.dsl.runtime.GenericModel;

public class SimpleStepMapTest {

    static GenericModel model = new GenericModel();

    static IntegerFieldInfo input = model.intField(123456, "input");
    static StringFieldInfo output = model.stringField("", "output");

    @Test
    public void test_converter_lambda_lift() {

        MappingRule mapping      = DOOV.map(input).using(Object::toString).to(output);
        ValidationRule validated = DOOV.when(output.eq("123456")).validate();

        mapping.executeOn(model,model);

        Assertions.assertThat(validated.executeOn(model).value()).isEqualTo(true);
    }
}
