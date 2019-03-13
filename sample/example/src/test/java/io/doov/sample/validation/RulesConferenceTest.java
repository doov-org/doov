package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.sample.validation.RulesConference.userAccount;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.Result;
import io.doov.sample.model.SampleModel;
import io.doov.sample.model.SampleModels;

public class RulesConferenceTest {

    private SampleModel model;

    @BeforeEach
    public void before() {
        model = SampleModels.sample();
    }

    @Test
    public void should_default_user_account_validates() {
        Result result = userAccount.executeOn(model);
        assertThat(result).isTrue().hasNoFailureCause();
    }

    @Test
    public void should_user_account_too_young_fail() {
        model.getAccount().setPhoneNumber(null);
        Result result = userAccount.executeOn(model);
        assertThat(result).isFalse().hasFailureCause("account phone number starts with '+33'",
                Locale.US);
    }

}
