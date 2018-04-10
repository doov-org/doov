/*
 * Copyright (C) by Courtanet, All Rights Reserved.
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

import org.junit.jupiter.api.*;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.*;

/**
 * Validate that a profile<br>
 * has at least 18 years when their coutry is France and their phone number does not start by '+33'<br>
 * has at least 21 years when their coutry is Canadian and their phone number does not start by '+1'<br>
 */
public class FailureCauseSample3Test {
    private final ValidationRule rule = DOOV
                    .when(matchAny(matchAll(userBirthdate.ageAt(today()).greaterThan(18),
                                    accountCountry.eq(Country.FR),
                                    accountPhoneNumber.startsWith("+33")),
                                    matchAll(userBirthdate.ageAt(today()).greaterThan(21),
                                                    accountCountry.eq(Country.CAN),
                                                    accountPhoneNumber.startsWith("+1"))))
                    .validate();

    private final SampleModel beanModel = new SampleModel();
    private final DslModel wrapperModel = new SampleModelWrapper(beanModel);

    @BeforeEach
    public void plaintText() {
        System.out.print(rule.readable());
    }

    @AfterEach
    public void blankline() {
        System.out.println("");
    }

    @Test
    public void getFailureCause_setup_1() {
        Result result = rule.withShortCircuit(false).executeOn(wrapperModel);
        assertThat(result).isFalse();
        System.out.println("> " + result.getFailureCause());
    }

    @Test
    public void getFailureCause_setup_3() {
        beanModel.getUser().setBirthDate(LocalDate.now().minusYears(22));

        Result result = rule.withShortCircuit(false).executeOn(wrapperModel);
        assertThat(result).isFalse();

        System.out.println("> " + result.getFailureCause());
    }

    @Test
    public void getFailureCause_setup_4() {
        beanModel.getUser().setBirthDate(LocalDate.now().minusYears(22));
        beanModel.getAccount().setCountry(Country.FR);

        Result result = rule.withShortCircuit(false).executeOn(wrapperModel);
        assertThat(result).isFalse();

        System.out.println("> " + result.getFailureCause());
    }

    @Test
    public void getFailureCause_setup_5() {
        beanModel.getUser().setBirthDate(LocalDate.now().minusYears(22));
        beanModel.getAccount().setCountry(Country.FR);
        beanModel.getAccount().setPhoneNumber("+33 1 23 45 67 89");

        Result result = rule.withShortCircuit(false).executeOn(wrapperModel);
        assertThat(result).isTrue();

        System.out.println("> " + result.getFailureCause());
    }
}
