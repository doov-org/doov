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

import io.doov.core.dsl.field.types.BooleanFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

/**
 * @see BooleanCondition
 */
public class BooeanConditionTest {
    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private BooleanFieldInfo A = model.booleanField(true, "A"),
            B = model.booleanField(false, "B"),
            C = model.booleanField(false, "C");
    private Result result;
    private Metadata reduce;

    @Test
    void not() {
        rule = when(A.not()).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A not validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A not");
    }

    @Test
    void and_value() {
        rule = when(A.and(false)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (A and false) validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A and false");
    }

    @Test
    void and_field() {
        rule = when(A.and(B)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (A and B) validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A and B");
    }

    @Test
    void or_value() {
        rule = when(B.or(false)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (B or false) validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("B or false");
    }

    @Test
    void or_field() {
        rule = when(B.or(C)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (B or C) validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("B or C");
    }

    @Test
    void isTrue() {
        rule = when(B.isTrue()).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when B is true validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("B is true");
    }

    @Test
    void isFalse() {
        rule = when(A.isFalse()).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A is false validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A is false");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule + " -> " + reduce);
    }
}
