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
package io.doov.core.dsl.impl.time;

import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfYear;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

/**
 * @see TemporalFunction
 */
public class TemporalFunctionTest {
    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private LocalDateFieldInfo A = model.localDateField(LocalDate.now(), "A");
    private IntegerFieldInfo B = model.intField(1, "B");
    private Result result;
    private Metadata reduce;

    @Test
    void with() {
        rule = when(A.with(firstDayOfYear()).eq(LocalDate.of(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A with first day of year = 0001-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A with first day of year = 0001-01-01");
    }

    @Test
    void minus_value() {
        rule = when(A.minus(1, ChronoUnit.DAYS).eq(LocalDate.of(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A minus 1 day(s) = 0001-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A minus 1 day(s) = 0001-01-01");
    }

    @Test
    void minus_field() {
        rule = when(A.minus(B, ChronoUnit.DAYS).eq(LocalDate.of(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A minus B day(s) = 0001-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A minus B day(s) = 0001-01-01");
    }

    @Test
    void plus_value() {
        rule = when(A.plus(1, ChronoUnit.DAYS).eq(LocalDate.of(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A + 1 day(s) = 0001-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A + 1 day(s) = 0001-01-01");
    }

    @Test
    void plus_field() {
        rule = when(A.plus(B, ChronoUnit.DAYS).eq(LocalDate.of(1, 1, 1))).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A + B day(s) = 0001-01-01 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A + B day(s) = 0001-01-01");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule + " -> " + reduce);
    }
}
