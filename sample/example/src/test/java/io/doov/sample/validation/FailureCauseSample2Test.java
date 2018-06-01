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
package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.dsl.DslSampleModel.accountCountry;
import static io.doov.sample.field.dsl.DslSampleModel.accountPhoneNumber;
import static io.doov.sample.field.dsl.DslSampleModel.userBirthdate;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.*;

import io.doov.core.dsl.lang.Result;
import io.doov.sample.field.dsl.DslSampleModel;
import io.doov.sample.field.dsl.DslSampleModel.SampleModelRule;
import io.doov.sample.model.Country;
import io.doov.sample.model.SampleModel;

/**
 * Validate that a profile<br>
 * has at least 18 years when their country is France and their phone number starts with '+33'<br>
 * has at least 21 years when their country is Canadian and their phone number starts with '+1'<br>
 */
public class FailureCauseSample2Test {
    private final SampleModelRule rule = DslSampleModel
            .when(userBirthdate.ageAt(today()).greaterThan(18)
                    .and(accountCountry.eq(Country.FR).and(accountPhoneNumber.startsWith("+33")))
                    .or(userBirthdate.ageAt(today()).greaterThan(21)
                            .and(accountCountry.eq(Country.CAN)
                                    .and(accountPhoneNumber.startsWith("+1")))))
            .validate();

    private final Locale locale = Locale.FRENCH;
    private final SampleModel model = new SampleModel();

    @BeforeEach
    public void plaintText() {
        System.out.println(rule.readable(locale));
    }

    @AfterEach
    public void blankline() {
        System.out.println("");
    }

    @Test
    public void getFailureCause_setup_1() {
        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result).isFalse();
        System.out.println("> " + result.getFailureCause(locale));
    }

    @Test
    public void getFailureCause_setup_3() {
        model.getUser().setBirthDate(LocalDate.now().minusYears(22));

        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result).isFalse();

        System.out.println("> " + result.getFailureCause(locale));
    }

    @Test
    public void getFailureCause_setup_4() {
        model.getUser().setBirthDate(LocalDate.now().minusYears(22));
        model.getAccount().setCountry(Country.FR);

        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result).isFalse();

        System.out.println("> " + result.getFailureCause(locale));
    }

    @Test
    public void getFailureCause_setup_5() {
        model.getUser().setBirthDate(LocalDate.now().minusYears(22));
        model.getAccount().setCountry(Country.FR);
        model.getAccount().setPhoneNumber("+33 1 23 45 67 89");

        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result).isTrue();

        System.out.println("> " + result.getFailureCause(locale));
    }
}
