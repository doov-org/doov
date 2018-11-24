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
package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.dsl.DslSampleModel.accountCountry;
import static io.doov.sample.field.dsl.DslSampleModel.accountEmail;
import static io.doov.sample.field.dsl.DslSampleModel.accountPhoneNumber;
import static io.doov.sample.field.dsl.DslSampleModel.userBirthdate;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.*;

import io.doov.core.dsl.lang.ReduceType;
import io.doov.core.dsl.lang.Result;
import io.doov.sample.field.dsl.DslSampleModel;
import io.doov.sample.field.dsl.DslSampleModel.SampleModelRule;
import io.doov.sample.model.Country;
import io.doov.sample.model.SampleModel;

/**
 * Validate that a profile has an account email is less than 20 characters <br>
 * has at least 18 years when their country is France<br>
 * their country is France when their phone number starts with '+33'
 */
public class FailureCauseSample1Test {

    private final SampleModelRule rule = DslSampleModel.when(accountEmail.length().lesserThan(20)
            .and(userBirthdate.ageAt(today()).greaterThan(18).and(accountCountry.eq(Country.FR)))
            .and(accountCountry.eq(Country.FR).and(accountPhoneNumber.startsWith("+33"))))
            .validate();

    private final Locale locale = Locale.US;
    private final SampleModel model = new SampleModel();

    @BeforeEach
    public void plaintText() {
        System.out.println(rule.readable(locale));
    }

    @AfterEach
    public void blankline() {
        System.out.println();
    }

    @Test
    public void getFailureCause_setup_1() {
        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result)
                .isFalse()
                .hasFailureCause("(account email length is < 20 and (user birthdate age at today > 18 and account " +
                                "country = FR)) and (account country = FR and account phone number starts with '+33')",
                        locale);
        System.out.println("> " + result.getFailureCause(locale));
    }

    @Test
    public void getFailureCause_setup_2() {
        model.getAccount().setEmail("test@test.org");

        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result)
                .isFalse()
                .hasFailureCause("(user birthdate age at today > 18 and account country = FR) and (account country = " +
                        "FR and account phone number starts with '+33')", locale);
        System.out.println("> " + result.getFailureCause(locale));
    }

    @Test
    public void getFailureCause_setup_3() {
        model.getAccount().setEmail("test@test.org");
        model.getUser().setBirthDate(LocalDate.now().minusYears(19));

        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result)
                .isFalse()
                .hasFailureCause("account country = FR and (account country = FR and account phone number starts with" +
                        " '+33')", locale);

        System.out.println("> " + result.getFailureCause(locale));
    }

    @Test
    public void getFailureCause_setup_4() {
        model.getAccount().setEmail("test@test.org");
        model.getUser().setBirthDate(LocalDate.now().minusYears(19));
        model.getAccount().setCountry(Country.FR);

        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result)
                .isFalse()
                .hasFailureCause("account phone number starts with '+33'", locale);

        System.out.println("> " + result.getFailureCause(locale));
    }

    @Test
    public void getFailureCause_setup_5() {
        model.getAccount().setEmail("test@test.org");
        model.getUser().setBirthDate(LocalDate.now().minusYears(19));
        model.getAccount().setCountry(Country.FR);
        model.getAccount().setPhoneNumber("+33 1 23 45 67 89");

        Result result = rule.withShortCircuit(false).executeOn(model);

        assertThat(result)
                .isTrue()
                .hasNoFailureCause()
                .hasReduceMessage("(account email length is < 20 and (user birthdate age at today > 18 and account " +
                        "country = FR)) and (account country = FR and account phone number starts with '+33')", locale);

        System.out.println("> " + result.reduceMessage(locale, ReduceType.SUCCESS));
    }

}
