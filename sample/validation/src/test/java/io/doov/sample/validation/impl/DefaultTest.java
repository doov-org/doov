package io.doov.sample.validation.impl;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.sample.field.SampleFieldIdInfo.accountCountry;
import static io.doov.sample.field.SampleFieldIdInfo.accountLogin;
import static io.doov.sample.field.SampleFieldIdInfo.userFirstName;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DslModel;
import io.doov.sample.model.*;

public class DefaultTest {

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
    public void should_equality_works_like_java() {
        assertThat(accountLogin().isNull()).validates(model);
        assertThat(accountLogin().isNotNull()).doesNotValidate(model);

        account.setLogin("toto");
        assertThat(accountLogin().eq("toto")).validates(model);
        assertThat(accountLogin().eq(() -> "toto")).validates(model);

        user.setFirstName("toto");
        assertThat(accountLogin().eq(userFirstName())).validates(model);

        user.setFirstName("titi");
        assertThat(accountLogin().eq(userFirstName())).doesNotValidate(model);
        assertThat(accountLogin().notEq(userFirstName())).validates(model);
        assertThat(accountLogin().notEq("todo")).validates(model);

        account.setCountry(Country.FR);
        assertThat(accountCountry().eq(Country.FR)).validates(model);
    }

}
