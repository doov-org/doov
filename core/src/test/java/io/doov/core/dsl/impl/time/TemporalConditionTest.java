/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.time;

import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;

/**
 * @see TemporalCondition
 */
public class TemporalConditionTest {
    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private LocalDateFieldInfo A = model.localDateField(LocalDate.now(), "A"),
            B = model.localDateField(LocalDate.now().plusDays(1), "B");
    private Result result;
    private Metadata reduce;

    @Test
    void eq_condition() {
        rule = when(A.eq(B)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A = B validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A = B");
    }

    @Test
    void before_value() {
        rule = when(A.before(LocalDate.of(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A before 0001-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A before 0001-01-01");
    }

    @Test
    void before_field() {
        rule = when(B.before(A)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when B before A validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("B before A");
    }

    @Test
    void before_supplier() {
        rule = when(A.before(() -> LocalDate.of(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A before 0001-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A before 0001-01-01");
    }

    @Test
    void before_condition() {
        rule = when(A.before(LocalDateSuppliers.date(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A before '0001-01-01' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A before '0001-01-01'");
    }

    @Test
    void beforeOrEq_value() {
        rule = when(A.beforeOrEq(LocalDate.of(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A before or equals 0001-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A before or equals 0001-01-01");
    }

/*
    @Test
    @Disabled
    void beforeOrEq_field() {
        rule = when(B.beforeOrEq(A)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when B before or equals A validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("B before or equals A");
    }
*/

    @Test
    void beforeOrEq_supplier() {
        rule = when(A.beforeOrEq(() -> LocalDate.of(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A before or equals 0001-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A before or equals 0001-01-01");
    }

    @Test
    void beforeOrEq_condition() {
        rule = when(A.beforeOrEq(LocalDateSuppliers.date(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A before or equals '0001-01-01' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A before or equals '0001-01-01'");
    }

    @Test
    void after_value() {
        rule = when(A.after(LocalDate.of(2100, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A after 2100-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A after 2100-01-01");
    }

    @Test
    void after_field() {
        rule = when(A.after(B)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A after B validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A after B");
    }

    @Test
    void after_supplier() {
        rule = when(A.after(() -> LocalDate.of(2100, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A after 2100-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A after 2100-01-01");
    }

    @Test
    void after_condition() {
        rule = when(A.after(LocalDateSuppliers.date(2100, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A after '2100-01-01' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A after '2100-01-01'");
    }

    @Test
    void afterOrEq_value() {
        rule = when(A.afterOrEq(LocalDate.of(2100, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A after or equals 2100-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A after or equals 2100-01-01");
    }

/*
    @Test
    @Disabled
    void afterOrEq_field() {
        rule = when(A.afterOrEq(B)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A after or equals B validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A after or equals B");
    }
*/

    @Test
    void afterOrEq_supplier() {
        rule = when(A.afterOrEq(() -> LocalDate.of(2100, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A after or equals 2100-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A after or equals 2100-01-01");
    }

    @Test
    void afterOrEq_condition() {
        rule = when(A.afterOrEq(LocalDateSuppliers.date(2100, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A after or equals '2100-01-01' validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A after or equals '2100-01-01'");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule + " -> " + reduce);
    }
}
