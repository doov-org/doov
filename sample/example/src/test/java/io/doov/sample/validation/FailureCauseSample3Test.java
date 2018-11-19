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
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.dsl.DslSampleModel.accountCountry;
import static io.doov.sample.field.dsl.DslSampleModel.accountPhoneNumber;
import static io.doov.sample.field.dsl.DslSampleModel.userBirthdate;

import java.time.LocalDate;
import java.util.Locale;

import org.assertj.core.api.Assertions;
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
public class FailureCauseSample3Test {

    private final SampleModelRule rule = DslSampleModel
            .when(matchAny(matchAll(userBirthdate.ageAt(today()).greaterThan(18),
                    accountCountry.eq(Country.FR),
                    accountPhoneNumber.startsWith("+33")),
                    matchAll(userBirthdate.ageAt(today()).greaterThan(21),
                            accountCountry.eq(Country.CAN),
                            accountPhoneNumber.startsWith("+1"))))
            .validate();

    private final Locale locale = Locale.FRANCE;
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
        assertThat(result)
                .isFalse()
                .hasFailureCause("correspond à au moins un [correspond à tous [la date de naissance âge à la date du " +
                        "jour > 18, le pays = FR, le numéro de téléphone commence par '+33'], correspond à tous [la " +
                        "date de naissance âge à la date du jour > 21, le pays = CAN, le numéro de téléphone commence" +
                        " par '+1']]", locale);
        System.out.println("> " + result.getFailureCause(locale));
    }

    @Test
    public void getFailureCause_setup_3() {
        model.getUser().setBirthDate(LocalDate.now().minusYears(22));

        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result)
                .isFalse()
                .hasFailureCause("correspond à au moins un [correspond à tous [le pays = FR, le numéro de téléphone " +
                        "commence par '+33'], correspond à tous [le pays = CAN, le numéro de téléphone commence par " +
                        "'+1']]", locale);

        System.out.println("> " + result.getFailureCause(locale));
    }

    @Test
    public void getFailureCause_setup_4() {
        model.getUser().setBirthDate(LocalDate.now().minusYears(22));
        model.getAccount().setCountry(Country.FR);

        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result)
                .isFalse()
                .hasFailureCause("correspond à au moins un [le numéro de téléphone commence par '+33', correspond à " +
                        "tous [le pays = CAN, le numéro de téléphone commence par '+1']]", locale);

        System.out.println("> " + result.getFailureCause(locale));
    }

    @Test
    public void getFailureCause_setup_5() {
        model.getUser().setBirthDate(LocalDate.now().minusYears(22));
        model.getAccount().setCountry(Country.FR);
        model.getAccount().setPhoneNumber("+33 1 23 45 67 89");

        Result result = rule.withShortCircuit(false).executeOn(model);
        assertThat(result)
                .isTrue()
                .hasNoFailureCause()
                .hasReduceMessage("correspond à tous [la date de naissance âge à la date du jour > 18, le " +
                "pays = FR, le numéro de téléphone commence par '+33']", locale);

        System.out.println("> " + result.reduce(locale));
    }
}
