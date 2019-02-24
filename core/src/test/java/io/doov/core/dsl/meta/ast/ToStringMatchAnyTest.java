/*
 * Copyright 2018 Courtanet
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
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.*;

import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;

public class ToStringMatchAnyTest {
    private static final Locale LOCALE = Locale.US;
    private StepCondition A, B, C, D;
    private ValidationRule rule;

    @Test
    void matchAny_true_false_false_complex() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        D = alwaysFalse("D");
        rule = when(matchAny(A.or(D), B, C)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when match any [(always true A or always false D), always false B, always false C] validate");
    }

    @Test
    void matchAny_false_true_true_complex() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        D = alwaysTrue("D");
        rule = when(matchAny(A, B, C.and(D))).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when match any [always false A, always true B, (always true C and always true D)] validate");
    }

    @Test
    void matchAny_false_false_false() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        rule = when(matchAny(A, B, C)).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when match any [always false A, always false B, always false C] validate");
    }

    @Test
    void matchAny_false_false_false_complex() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        D = alwaysFalse("D");
        rule = when(matchAny(A, B, C.and(D))).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when match any [always false A, always false B, (always true C and always false D)] validate");
    }

    @Test
    void matchAny_true_false_false() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        rule = when(matchAny(A, B, C)).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when match any [always true A, always false B, always false C] validate");
    }

    @Test
    void matchAny_false_true_true() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        rule = when(matchAny(A, B, C)).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when match any [always false A, always true B, always true C] validate");
    }

    @Test
    void matchAny_true_true_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        rule = when(matchAny(A, B, C)).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when match any [always true A, always true B, always true C] validate");
    }

    @Test
    @Disabled
    // FIXME
    void matchAny_field_true_true_true_failure() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo something = model.stringField("something", "string field");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = something.matches("^some.*");
        rule = when(matchAny(A, B, C)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when match any [zero < 4, yesterday before today, string field matches '^some.*'] validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule);
    }
}
