/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.num;

import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

/**
 * @see NumericCondition
 */
public class NumericConditionTest {
    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private IntegerFieldInfo A = model.intField(1, "A"),
            B = model.intField(2, "B");
    private Result result;
    private Metadata reduce;

    @Test
    void lesserThan_value() {
        rule = when(A.lesserThan(0)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A < 0 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A < 0");
    }

    @Test
    void lesserThan_field() {
        rule = when(B.lesserThan(A)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when B < A validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("B < A");
    }

    @Test
    void lesserOrEquals_value() {
        rule = when(A.lesserOrEquals(0)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A <= 0 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A <= 0");
    }

    @Test
    void lesserOrEquals_field() {
        rule = when(B.lesserOrEquals(A)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when B <= A validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("B <= A");
    }

    @Test
    void greaterThan_value() {
        rule = when(A.greaterThan(2)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A > 2 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A > 2");
    }

    @Test
    void greaterThan_field() {
        rule = when(A.greaterThan(B)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A > B validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A > B");
    }

    @Test
    void greaterOrEquals_value() {
        rule = when(A.greaterOrEquals(2)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A >= 2 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A >= 2");
    }

    @Test
    void greaterOrEquals_field() {
        rule = when(A.greaterOrEquals(B)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A >= B validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A >= B");
    }
    
    @AfterEach
    void afterEach() {
        System.out.println(rule + " -> " + reduce);
    }
}
