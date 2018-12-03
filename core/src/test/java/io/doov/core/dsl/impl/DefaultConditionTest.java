/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

/**
 * @see DefaultCondition
 */
public class DefaultConditionTest {
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
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A = -function- -function- validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A = -function- -function-");
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

/*
    @Test
    @Disabled
    void notEq_supplier() {
        rule = when(A.notEq(() -> "value")).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A != -function- validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A != -function-");
    }
*/

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
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match any  : a, b, c validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A = 'value'");
    }

    @Test
    void anyMatch_predicate() {
        rule = when(A.anyMatch(v -> v.equals("a"))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match any -function- -function- validate");
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
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match all -function- -function- validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A match all -function- -function-");
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
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A match none -function- -function- validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A match none -function- -function-");
    }


    @AfterEach
    void afterEach() {
        System.out.println(rule + " -> " + reduce);
    }
}
