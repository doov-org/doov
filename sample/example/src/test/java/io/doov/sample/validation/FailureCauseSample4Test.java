/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.sample.field.dsl.DslSampleModel.accountCountry;

import org.junit.jupiter.api.*;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.*;

/**
 * Validate that a profile country is French or Canadian
 */
public class FailureCauseSample4Test {
    private final ValidationRule rule = DOOV.when(accountCountry.anyMatch(Country.CAN, Country.FR))
                    .validate();

    private final SampleModel model = new SampleModel();
    private final DslModel wrapper = new SampleModelWrapper(model);

    @BeforeEach
    public void plaintText() {
        System.out.print(rule.readable());
    }

    @AfterEach
    public void blankline() {
        System.out.println("");
    }

    @Test
    @Disabled
    public void getFailureCause_setup_0() {

        Result result = rule.withShortCircuit(false).executeOn(wrapper);
        assertThat(result).isFalse();
        System.out.println("> " + result.getFailureCause());
    }

    @Test
    public void getFailureCause_setup_1() {
        model.getAccount().setCountry(Country.UK);

        Result result = rule.withShortCircuit(false).executeOn(wrapper);
        assertThat(result).isFalse();
        System.out.println("> " + result.getFailureCause());
    }

    @Test
    public void getFailureCause_setup_2() {
        model.getAccount().setCountry(Country.CAN);

        Result result = rule.withShortCircuit(false).executeOn(wrapper);
        assertThat(result).isTrue();
        System.out.println("> " + result.getFailureCause());
    }
}
