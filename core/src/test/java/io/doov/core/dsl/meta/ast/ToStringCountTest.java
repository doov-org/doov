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

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;

public class ToStringCountTest {
    private static final Locale LOCALE = Locale.US;
    private StepCondition A, B;
    private ValidationRule rule;

    @Test
    @Disabled
    // FIXME
    void count_false_false() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        rule = when(count(A, B).greaterThan(1)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (count [always false A, always false B] > 1) validate");
    }

    @Test
    @Disabled
    // FIXME
    void count_true_false_greaterThan() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        rule = when(count(A, B).greaterThan(1)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (count [always true A, always false B] > 1) validate");
    }

    @Test
    @Disabled
    // FIXME
    void count_true_false_greaterOrEquals() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        rule = when(count(A, B).greaterOrEquals(1)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (count [always true A, always false B] >= 1) validate");
    }

    @Test
    @Disabled
    // FIXME
    void count_true_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        rule = when(count(A, B).greaterThan(1)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (count [always true A, always true B] > 1) validate");
    }

    @Test
    @Disabled
    // FIXME
    void count_field_true_true_failure() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        rule = when(count(A, B).greaterThan(1)).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when (count [zero < 4, yesterday before today] > 1) validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule);
    }
}
