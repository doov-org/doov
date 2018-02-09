package io.doov.sample.validation.impl;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.sample.field.dsl.DSLSampleModel.accountAcceptEmail;
import static io.doov.sample.field.dsl.DSLSampleModel.configurationMailingCampaign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DslModel;
import io.doov.sample.model.*;

public class BooleanTest {

    private DslModel model;
    private Account account;
    private Configuration configuration;

    @BeforeEach
    public void before() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setAccount(account = new Account());
        sampleModel.setConfiguration(configuration = new Configuration());
        model = new SampleModelWrapper(sampleModel);
    }

    @Test
    public void should_logical_truthness_works_like_java() {
        assertThat(accountAcceptEmail().isNotNull()).validates(model);

        assertThat(accountAcceptEmail().isFalse()).validates(model);
        assertThat(accountAcceptEmail().not()).validates(model);

        account.setAcceptEmail(true);
        assertThat(accountAcceptEmail().isTrue()).validates(model);
        assertThat(accountAcceptEmail().not()).doesNotValidate(model);
        assertThat(accountAcceptEmail().isTrue().not()).doesNotValidate(model);
    }

    @Test
    public void should_logical_and_works_like_java() {
        assertThat(accountAcceptEmail().and(false)).doesNotValidate(model);
        assertThat(accountAcceptEmail().and(true)).doesNotValidate(model);

        account.setAcceptEmail(true);
        assertThat(accountAcceptEmail().and(false)).doesNotValidate(model);
        assertThat(accountAcceptEmail().and(true)).validates(model);

        account.setAcceptEmail(false);
        assertThat(accountAcceptEmail().and(configurationMailingCampaign())).doesNotValidate(model);

        account.setAcceptEmail(true);
        assertThat(accountAcceptEmail().and(configurationMailingCampaign())).doesNotValidate(model);

        configuration.setMailingCampaign(false);
        assertThat(accountAcceptEmail().and(configurationMailingCampaign())).doesNotValidate(model);

        configuration.setMailingCampaign(true);
        assertThat(accountAcceptEmail().and(configurationMailingCampaign())).validates(model);
    }

    @Test
    public void should_logical_or_works_like_java() {
        assertThat(accountAcceptEmail().or(false)).doesNotValidate(model);
        assertThat(accountAcceptEmail().or(true)).validates(model);

        account.setAcceptEmail(true);
        assertThat(accountAcceptEmail().or(false)).validates(model);
        assertThat(accountAcceptEmail().or(true)).validates(model);

        account.setAcceptEmail(false);
        assertThat(accountAcceptEmail().or(configurationMailingCampaign())).doesNotValidate(model);

        account.setAcceptEmail(true);
        assertThat(accountAcceptEmail().or(configurationMailingCampaign())).validates(model);

        configuration.setMailingCampaign(false);
        assertThat(accountAcceptEmail().or(configurationMailingCampaign())).validates(model);

        configuration.setMailingCampaign(true);
        assertThat(accountAcceptEmail().or(configurationMailingCampaign())).validates(model);
    }

    @Test
    public void should_logical_xor_works_like_java() {
        assertThat(accountAcceptEmail().xor(false)).doesNotValidate(model);
        assertThat(accountAcceptEmail().xor(true)).validates(model);

        account.setAcceptEmail(true);
        assertThat(accountAcceptEmail().xor(false)).validates(model);
        assertThat(accountAcceptEmail().xor(true)).doesNotValidate(model);

        account.setAcceptEmail(false);
        assertThat(accountAcceptEmail().xor(configurationMailingCampaign())).doesNotValidate(model);

        account.setAcceptEmail(true);
        assertThat(accountAcceptEmail().xor(configurationMailingCampaign())).validates(model);

        configuration.setMailingCampaign(false);
        assertThat(accountAcceptEmail().xor(configurationMailingCampaign())).validates(model);

        configuration.setMailingCampaign(true);
        assertThat(accountAcceptEmail().xor(configurationMailingCampaign())).doesNotValidate(model);
    }

}
