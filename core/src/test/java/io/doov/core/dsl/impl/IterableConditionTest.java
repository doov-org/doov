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
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

/**
 * @see IterableCondition
 */
public class IterableConditionTest {
    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private IterableFieldInfo<String, Iterable<String>> A = model.iterableField(asList("a", "aa"), "A"),
            B = model.iterableField(null, "B");
    private Result result;
    private Metadata reduce;

    @Test
    void contains() {
        rule = when(A.contains("b")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A contains 'b' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A contains 'b'");
    }

    @Test
    void containsAll() {
        rule = when(A.containsAll("a", "b")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A contains 'a' , 'b' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A contains 'a' , 'b'");
    }

    @Test
    void isEmpty() {
        rule = when(A.isEmpty()).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A is empty validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A is empty");
    }

    @Test
    void isNotEmpty() {
        rule = when(B.isNotEmpty()).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when B is not empty validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("B is not empty");
    }

    @Test
    void hasSize() {
        rule = when(A.hasSize(1)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A has size 1 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A has size 1");
    }

    @Test
    void hasNotSize() {
        rule = when(A.hasNotSize(2)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A has not size 2 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A has not size 2");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule + " -> " + reduce);
    }
}
