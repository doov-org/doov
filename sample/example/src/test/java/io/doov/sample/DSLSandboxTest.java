/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.sample;

import static io.doov.sample.field.dsl.DslSampleModel.accountId;
import static io.doov.sample.field.dsl.DslSampleModel.accountPreferencesMail;
import static io.doov.sample.field.dsl.DslSampleModel.accountTimezone;
import static io.doov.sample.field.dsl.DslSampleModel.userBirthdate;
import static io.doov.sample.model.EmailType.ADMINISTRATOR;
import static io.doov.sample.model.EmailType.PRIVATE;
import static io.doov.sample.model.Timezone.ETC_GMT;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.SampleModels;

public class DSLSandboxTest {

    private FieldModel model = SampleModels.wrapper();

    @Test
    public void sample1() {
        ValidationRule rule = DOOV.when(accountId.eq(1L)).validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isFalse();
        assertThat(rule.executeOn(model).getFailureCause(Locale.US)).isEqualTo("account id = 1");
    }

    @Test
    public void sample2() {
        ValidationRule rule = DOOV.when(accountId.eq(1L)).validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isFalse();
        assertThat(rule.executeOn(model).getFailureCause());
    }

    @Test
    public void sample3() {
        ValidationRule rule = DOOV.when(accountId.eq(1L).not()).validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isTrue();
        assertThat(rule.executeOn(model).getFailureCause()).isNull();
    }

    @Test
    public void sample4() {
        ValidationRule rule = DOOV.when(userBirthdate.eq(LocalDate.of(1980, 8, 1))).validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isTrue();
        assertThat(rule.executeOn(model).getFailureCause()).isNull();
    }

    @Test
    public void sample5() {
        ValidationRule rule = DOOV.when(userBirthdate.between(LocalDate.of(1980, 1, 1), LocalDate.of(1980, 12, 31)))
                        .validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isTrue();
        assertThat(rule.executeOn(model).getFailureCause()).isNull();
    }

    @Test
    public void sample6() {
        ValidationRule rule = DOOV
                        .when(userBirthdate.between(LocalDate.of(1980, 1, 1), LocalDate.of(1980, 12, 31))
                                        .and(accountId.notEq(9L)).or((accountTimezone).eq(ETC_GMT)))
                        .validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isTrue();
        assertThat(rule.executeOn(model).getFailureCause()).isNull();
    }

    @Test
    public void sample7() {
        ValidationRule rule = DOOV.when(accountPreferencesMail.eq(EnumSet.of(ADMINISTRATOR, PRIVATE))).validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isTrue();
        assertThat(rule.executeOn(model).getFailureCause()).isNull();
    }

    @Test
    public void sample8() {
        ValidationRule rule;

        rule = DOOV.when(accountPreferencesMail.contains(ADMINISTRATOR)).validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isTrue();
        assertThat(rule.executeOn(model).getFailureCause()).isNull();

        rule = DOOV.when(accountPreferencesMail.containsAll(ADMINISTRATOR, PRIVATE)).validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isTrue();
        assertThat(rule.executeOn(model).getFailureCause()).isNull();
    }

    @Test
    public void sample9() {
        ValidationRule rule = DOOV.when(accountPreferencesMail.isNotEmpty()).validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isTrue();
        assertThat(rule.executeOn(model).getFailureCause()).isNull();
    }

    @Test
    public void sample10() {
        ValidationRule rule = DOOV.when(accountPreferencesMail.hasNotSize(1)).validate();
        System.out.println(rule.readable());
        assertThat(rule.executeOn(model).value()).isTrue();
        assertThat(rule.executeOn(model).getFailureCause()).isNull();
    }

}
