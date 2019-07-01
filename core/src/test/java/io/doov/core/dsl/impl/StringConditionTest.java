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
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

/**
 * @see io.doov.core.dsl.impl.base.StringFunction
 */
public class StringConditionTest {
    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private StringFieldInfo A = model.stringField("value", "A");
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

    @AfterEach
    void afterEach() {
        System.out.println(rule + " -> " + reduce);
    }
}
