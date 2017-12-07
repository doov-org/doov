package io.doov.sample.validation.impl;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.sample.field.SampleFieldIdInfo.accountLogin;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslModel;
import io.doov.sample.model.*;

public class StringTest {

    private DslModel model;
    private User user;
    private Account account;

    @BeforeEach
    public void before() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setUser(user = new User());
        sampleModel.setAccount(account = new Account());
        model = new SampleModelWrapper(sampleModel);
    }

    @Test
    public void should_string_function_works_like_java() {
        account.setLogin("toto");
        assertThat(accountLogin().matches("[a-z]+")).validates(model);

        account.setLogin("toto2");
        assertThat(accountLogin().matches("[a-z]+")).doesNotValidate(model);

        account.setLogin("toto");
        assertThat(accountLogin().contains("to")).validates(model);
        assertThat(accountLogin().contains("ti")).doesNotValidate(model);

        account.setLogin("toti");
        assertThat(accountLogin().startsWith("to")).validates(model);
        assertThat(accountLogin().startsWith("ti")).doesNotValidate(model);

        assertThat(accountLogin().endsWith("ti")).validates(model);
        assertThat(accountLogin().endsWith("to")).doesNotValidate(model);
    }

    @Test
    public void should_conversion_call_return_valid_condition() {
        account.setLogin("todo");
        assertThat(accountLogin().length().eq(4)).validates(model);

        assertThatThrownBy(() -> DOOV.when(accountLogin().parseInt().eq(4)).validate().executeOn(model))
                .isInstanceOf(NumberFormatException.class);

        account.setLogin("42");
        assertThat(accountLogin().parseInt().eq(42)).validates(model);
    }

}
