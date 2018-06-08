package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.Result;
import io.doov.sample.model.SampleModel;
import io.doov.sample.model.SampleModels;

public class RulesJdkIOTest {

    @Test
    public void test_account_rule() {
        SampleModel sample = SampleModels.sample();

        Result result = RulesJdkIO.account.executeOn(sample);

        assertThat(result).isTrue();
    }

    @Test
    public void test_account_rule_fails() {
        SampleModel sample = SampleModels.sample();
        sample.getAccount().setPhoneNumber(null);

        Result result = RulesJdkIO.account.executeOn(sample);

        assertThat(result)
                .isFalse()
                .hasFailureCause("account phone number starts with '+33'");
    }

    @Test
    public void print_rule() {
        System.out.println(RulesJdkIO.account.readable());
    }

}