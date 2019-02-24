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
import static io.doov.core.dsl.DOOV.count;
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

public class ToStringAndTest {
    private static final Locale LOCALE = Locale.US;
    private StepCondition A, B, C, D;
    private ValidationRule rule;

    @Test
    @Disabled
    // FIXME
    void and_false_false() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        rule = when(A.and(B)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (always false A and always false B) validate");
    }

    @Test
    @Disabled
    // FIXME
    void and_true_false() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        rule = when(A.and(B)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (always true A and always false B) validate");
    }

    @Test
    @Disabled
    // FIXME
    void and_false_true() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        rule = when(A.and(B)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (always false A and always true B) validate");
    }

    @Test
    @Disabled
    // FIXME
    void and_true_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        rule = when(A.and(B)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (always true A and always true B) validate");
    }

    @Test
    @Disabled
    // FIXME
    void and_field_true_true_failure() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        rule = when(A.and(B)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (zero < 4 and yesterday before today) validate");
    }

    @Test
    @Disabled
    // FIXME
    void and_and_and() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();
        rule = when(A.and(B).and(C).and(D)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when (((zero < 4 and yesterday before today) and name starts with 'B') and is True is false) validate");
    }

    @Test
    @Disabled
    // FIXME
    void and_and_count() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();

        rule = when(A.and(B.and(count(C, D).greaterThan(1)))).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when (zero < 4 and (yesterday before today and (count [name starts with 'B', is True is false] > 1))) validate");
    }

    @Test
    @Disabled
    // FIXME
    void and_or_and() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();
        rule = when(A.and(B).or(C.and(D))).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when ((zero < 4 and yesterday before today) or (name starts with 'B' and is True is false)) validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule);
    }
}
