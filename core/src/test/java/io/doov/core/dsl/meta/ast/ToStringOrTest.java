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

public class ToStringOrTest {
    private static final Locale LOCALE = Locale.US;
    GenericModel model = new GenericModel();
    private StepCondition A, B, C, D;
    private ValidationRule rule;
    private IntegerFieldInfo zero;
    private LocalDateFieldInfo yesterday;
    private StringFieldInfo bob;
    private BooleanFieldInfo is_true;

    @BeforeEach
    void beforeEach() {
        zero = model.intField(0, "zero");
        yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        bob = model.stringField("Bob", "name");
        is_true = model.booleanField(false, "is True");
    }

    @Test
    @Disabled
    // FIXME
    void or_true_false_complex() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        rule = when(A.or(B.or(C))).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when (always true A or (always false B or always true C)) validate");
    }

    @Test
    @Disabled
    // FIXME
    void or_false_true_complex() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        rule = when(A.or(B.and(C))).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when (always false A or (always true B and always true C)) validate");
    }

    @Test
    @Disabled
    // FIXME
    void or_false_false() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        rule = when(A.or(B)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (always false A or always false B) validate");
    }

    @Test
    @Disabled
    // FIXME
    void or_false_false_complex() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        rule = when(A.or(B.and(C))).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when (always false A or (always false B and always true C)) validate");
    }

    @Test
    @Disabled
    // FIXME
    void or_true_false() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        rule = when(A.or(B)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (always true A or always false B) validate");
    }

    @Test
    @Disabled
    // FIXME
    void or_false_true() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        rule = when(A.or(B)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (always false A or always true B) validate");
    }

    @Test
    @Disabled
    // FIXME
    void or_true_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        rule = when(A.or(B)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (always true A or always true B) validate");
    }

    @Test
    @Disabled
    // FIXME
    void or_or_or() {
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();
        rule = when(A.or(B).or(C).or(D)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when (((zero < 4 or yesterday before today) or name starts with 'B') or is True is false) validate");
    }

    @Test
    @Disabled
    // FIXME
    void or_field_true_true() {
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        rule = when(A.or(B)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (zero < 4 or yesterday before today) validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule);
    }
}
