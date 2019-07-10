/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.impl.base.StringFunction;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

/**
 * @see io.doov.core.dsl.impl.base.StringFunction
 */
class StringConditionTest {
    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private StringFieldInfo A = model.stringField("value", "A");
    private StringFieldInfo B = model.stringField(" value ", "B");
    private Result result;
    private Metadata reduce;

    @Test
    void contains() {
        rule = when(A.contains("zz")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A contains 'zz' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A contains 'zz'");
    }

    @Test
    void matches() {
        rule = when(A.matches("z+")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A matches 'z+' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A matches 'z+'");
    }

    @Test
    void startsWith() {
        rule = when(A.startsWith("zz")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A starts with 'zz' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A starts with 'zz'");
    }

    @Test
    void endsWith() {
        rule = when(A.endsWith("zz")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A ends with 'zz' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A ends with 'zz'");
    }

    @Test
    void trim() {
        StringFunction function = B.trim();
        String functionValue = function.read(model, new DefaultContext(function.metadata));

        assertThat(function.readable(LOCALE)).isEqualTo("B trim");
        assertThat(functionValue).isEqualTo("value");
    }

    @Test
    void replaceAll() {
        StringFunction function = B.replaceAll("value", "other");
        String functionValue = function.read(model, new DefaultContext(function.metadata));

        assertThat(function.readable(LOCALE)).isEqualTo("B replace all 'value' 'other'");
        assertThat(functionValue).isEqualTo(" other ");
    }

    @Test
    void substring() {
        StringFunction function = A.substring(1, 3);
        String functionValue = function.read(model, new DefaultContext(function.metadata));

        assertThat(function.readable(LOCALE)).isEqualTo("A substring  : 1, 3");
        assertThat(functionValue).isEqualTo("al");
    }

    @Test
    void upperCase() {
        StringFunction function = A.upperCase(LOCALE);
        String functionValue = function.read(model, new DefaultContext(function.metadata));

        assertThat(function.readable(LOCALE)).isEqualTo("A upper case en_US");
        assertThat(functionValue).isEqualTo("VALUE");
    }

    @Test
    void lowerCase() {
        StringFunction function = A.lowerCase(LOCALE);
        String functionValue = function.read(model, new DefaultContext(function.metadata));

        assertThat(function.readable(LOCALE)).isEqualTo("A lower case en_US");
        assertThat(functionValue).isEqualTo("value");
    }

    @Test
    void concat() {
        StringFunction function = A.concat("zz");
        String functionValue = function.read(model, new DefaultContext(function.metadata));

        assertThat(function.readable(LOCALE)).isEqualTo("A concat 'zz'");
        assertThat(functionValue).isEqualTo("valuezz");

        function = A.concat(B.getStringFunction());
        functionValue = function.read(model, new DefaultContext(function.metadata));

        assertThat(function.readable(LOCALE)).isEqualTo("A concat B");
        assertThat(functionValue).isEqualTo("value value ");
    }



    @AfterEach
    void afterEach() {
        System.out.println(rule + " -> " + reduce);
    }
}
