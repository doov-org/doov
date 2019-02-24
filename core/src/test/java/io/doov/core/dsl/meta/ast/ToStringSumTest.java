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

import static io.doov.core.dsl.DOOV.sum;
import static io.doov.core.dsl.DOOV.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.*;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;

public class ToStringSumTest {
    private static final Locale LOCALE = Locale.US;
    private final GenericModel model = new GenericModel();
    private IntegerFieldInfo A, B;
    private ValidationRule rule;

    @Test
    @Disabled
    // FIXME
    void sum_1_2_greaterThan_1() {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        rule = when(sum(A, B).greaterThan(1)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (sum [A, B] > 1) validate");
    }

    @Test
    @Disabled
    // FIXME
    void sum_1_2_greaterThan_3() {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        rule = when(sum(A, B).greaterThan(3)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when (sum [A, B] > 3) validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule);
    }
}
