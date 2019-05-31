/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.num;

import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.core.dsl.lang.ReduceType.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

class NumericFunctionTest {

    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private IntegerFieldInfo A = model.intField(1, "A"), B = model.intField(2, "B");
    private DoubleFieldInfo C = model.doubleField(3.0, "C"), D = model.doubleField(4.0, "D");
    private FloatFieldInfo E = model.floatField(4123f, "E"), F = model.floatField(27f, "F");
    private Result result;
    private Metadata reduce;

    @Test
    void plus_int() {
        rule = DOOV.when(A.plus(B).eq(0)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A + B = 0 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("= 0");
    }

    @Test
    void minus_int() {
        rule = DOOV.when(A.minus(B).eq(-1)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A - B = -1 validate");
        assertThat(reduce.readable(LOCALE)).isEqualTo("A - B = -1");
    }

    @Test
    void minus_double() {
        rule = DOOV.when(C.minus(D).eq(-1.0)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when C - D = -1.0 validate");
        assertThat(reduce.readable(LOCALE)).isEqualTo("C - D = -1.0");
    }

    @Test
    void plus_double() {
        rule = DOOV.when(C.plus(D).eq( 6.0)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when C + D = 6.0 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("= 6.0");
    }

    @Test
    void plus_float() {
        rule = DOOV.when(E.plus(F).lesserOrEquals(5000f)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when E + F <= 5000.0 validate");
        assertThat(reduce.readable(LOCALE)).isEqualTo("E + F <= 5000.0");
    }

    @Test
    void minus_float() {
        rule = DOOV.when(E.minus(F).eq( 5000f)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when E - F = 5000.0 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("= 5000.0");
    }
}
