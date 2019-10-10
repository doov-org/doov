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

import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

/**
 * @see DefaultFunction
 */
public class DefaultFunctionTest {
    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private StringFieldInfo A = model.stringField("value", "A"),
            B = model.stringField(null, "B"),
            C = model.stringField("value", "C");
    private Result result;
    private Metadata reduce;

    @Test
    void isNull() {
        rule = when(A.isNull()).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A is null validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A is null");
    }

    @Test
    void isNotNull() {
        rule = when(B.isNotNull()).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when B is not null validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("B is not null");
    }

    @Test
    void eq_value() {
        rule = when(A.eq("something")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A = 'something' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A = 'something'");
    }

    @Test
    void eq_supplier() {
        rule = when(A.eq(() -> "something")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A = -function- validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A = -function-");
    }

    @Test
    void eq_field() {
        rule = when(A.eq(B)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A = B validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A = B");
    }

    @Test
    void notEq_value() {
        rule = when(A.notEq("value")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A != 'value' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A != 'value'");
    }

    @Test
    void notEq_supplier() {
        rule = when(A.notEq(() -> "value")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A != -function- validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A != -function-");
    }

    @Test
    void notEq_field() {
        rule = when(A.notEq(C)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A != C validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A != C");
    }

    @Test
    void anyMatch_values() {
        rule = when(A.anyMatch("a", "b", "c")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match any [a, b, c] validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A = 'value'");
    }

    @Test
    void anyMatch_predicate() {
        rule = when(A.anyMatch(v -> v.equals("a"), "equals a")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match any [equals a] validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A = 'value'");
    }

    @Test
    void allMatch_values() {
        rule = when(A.allMatch("value", "b", "c")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match all  : value, b, c validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A match all  : value, b, c");
    }

    @Test
    void allMatch_collection() {
        rule = when(A.allMatch(asList("value", "b", "c"))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match all  : value, b, c validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A match all  : value, b, c");
    }

    @Test
    void allMatch_predicate() {
        rule = when(A.allMatch(v -> v.equals("a"))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match all -function- validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A match all -function-");
    }
    
    @Test
    void noneMatch_values() {
        rule = when(A.noneMatch("value", "b", "c")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match none  : value, b, c validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A match none  : value, b, c");
    }

    @Test
    void noneMatch_collection() {
        rule = when(A.noneMatch(asList("value", "b", "c"))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match none  : value, b, c validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A match none  : value, b, c");
    }

    @Test
    void noneMatch_predicate() {
        rule = when(A.noneMatch(v -> v.equals("value"))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match none -function- validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A match none -function-");
    }


    @AfterEach
    void afterEach() {
        System.out.println(rule + " -> " + reduce);
    }
}
