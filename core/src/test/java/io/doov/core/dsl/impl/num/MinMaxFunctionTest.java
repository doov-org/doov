/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.num;

import static io.doov.core.dsl.DOOV.max;
import static io.doov.core.dsl.DOOV.min;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

class MinMaxFunctionTest {

    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private IntegerFieldInfo A = model.intField(1, "A"),
            B = model.intField(2, "B");
    private DoubleFieldInfo C = model.doubleField(3.0, "C"), D = model.doubleField(4.0, "D");
    private FloatFieldInfo E = model.floatField(4123f, "E"), F = model.floatField(27f, "F");
    private Result result;
    private Metadata reduce;

    @Test
    void should_min_int_fields() {
        rule = when(min(A, B).eq(1)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (min [A, B] = 1) validate");
    }

    @Test
    void should_min_int_functions() {
        rule = when(min(A.getNumericFunction(), B.getNumericFunction()).eq(1)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (min [A, B] = 1) validate");
    }

    @Test
    void should_min_double_fields() {
        rule = when(min(C, D).eq(3.0)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (min [C, D] = 3.0) validate");
    }

    @Test
    void should_min_double_functions() {
        rule = when(min(D.getNumericFunction(), C.getNumericFunction()).eq(3.0)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (min [D, C] = 3.0) validate");
    }

    @Test
    void should_min_float_fields() {
        rule = when(min(E, F).eq(27f)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (min [E, F] = 27.0) validate");
    }

    @Test
    void should_min_float_functions() {
        rule = when(min(F.getNumericFunction(), E.getNumericFunction()).eq(27f)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (min [F, E] = 27.0) validate");
    }


    @Test
    void should_max_int_fields() {
        rule = when(max(A, B).eq(2)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (max [A, B] = 2) validate");
    }

    @Test
    void should_max_int_functions() {
        rule = when(max(A.getNumericFunction(), B.getNumericFunction()).eq(2)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (max [A, B] = 2) validate");
    }

    @Test
    void should_max_double_fields() {
        rule = when(max(C, D).eq(4.0)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (max [C, D] = 4.0) validate");
    }

    @Test
    void should_max_double_functions() {
        rule = when(max(D.getNumericFunction(), C.getNumericFunction()).eq(4.0)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (max [D, C] = 4.0) validate");
    }

    @Test
    void should_max_float_fields() {
        rule = when(max(E, F).eq(4123f)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (max [E, F] = 4123.0) validate");
    }

    @Test
    void should_max_float_functions() {
        rule = when(max(F.getNumericFunction(), E.getNumericFunction()).eq(4123f)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (max [F, E] = 4123.0) validate");
    }
}
