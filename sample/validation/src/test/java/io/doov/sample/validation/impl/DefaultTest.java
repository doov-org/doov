package io.doov.sample.validation.impl;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.sample.field.dsl.DslSampleModel.accountCountry;
import static io.doov.sample.field.dsl.DslSampleModel.accountLogin;
import static io.doov.sample.field.dsl.DslSampleModel.userFirstName;

import java.util.EnumSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.sample.model.*;
import io.doov.sample.wrapper.SampleModelWrapper;

public class DefaultTest {

    private FieldModel model;
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
    public void should_null_works_like_java() {
        assertThat(accountLogin.isNull()).validates(model);
        assertThat(accountLogin.isNotNull()).doesNotValidate(model);
    }

    @Test
    public void should_equality_works_like_java() {
        account.setLogin("toto");
        assertThat(accountLogin.eq("toto")).validates(model);
        assertThat(accountLogin.eq(() -> "toto")).validates(model);

        user.setFirstName("toto");
        assertThat(accountLogin.eq(userFirstName)).validates(model);

        user.setFirstName("titi");
        assertThat(accountLogin.eq(userFirstName)).doesNotValidate(model);
        assertThat(accountLogin.notEq(userFirstName)).validates(model);
        assertThat(accountLogin.notEq("todo")).validates(model);

        account.setCountry(Country.FR);
        assertThat(accountCountry.eq(Country.FR)).validates(model);
    }

    @Test
    public void should_any_match_works_like_java() {
        assertThat(accountLogin.anyMatch("toto")).doesNotValidate(model);

        account.setLogin("toto");
        assertThat(accountLogin.anyMatch("toto")).validates(model);

        account.setLogin("titi");
        assertThat(accountLogin.anyMatch("toto", "titi")).validates(model);

        account.setCountry(Country.FR);
        assertThat(accountCountry.anyMatch(EnumSet.of(Country.CAN, Country.UK))).doesNotValidate(model);
        assertThat(accountCountry.anyMatch(EnumSet.of(Country.CAN, Country.UK, Country.FR))).validates(model);

        assertThat(accountCountry.anyMatch(country -> country.name().equals("FR"))).validates(model);
    }

    @Test
    public void should_all_match_works_like_java() {
        assertThat(accountLogin.allMatch("toto")).doesNotValidate(model);

        account.setLogin("toto");
        assertThat(accountLogin.allMatch("toto")).validates(model);

        account.setLogin("titi");
        assertThat(accountLogin.allMatch("toto", "titi")).doesNotValidate(model);

        account.setCountry(Country.FR);
        assertThat(accountCountry.allMatch(EnumSet.of(Country.CAN, Country.FR))).doesNotValidate(model);
        assertThat(accountCountry.allMatch(EnumSet.of(Country.FR))).validates(model);

        assertThat(accountCountry.allMatch(country -> country.name().equals("FR"))).validates(model);
    }

    @Test
    public void should_none_match_works_like_java() {
        assertThat(accountLogin.noneMatch("toto")).doesNotValidate(model);

        account.setLogin("toto");
        assertThat(accountLogin.noneMatch("toto")).doesNotValidate(model);

        account.setLogin("titi");
        assertThat(accountLogin.noneMatch("toto", "titi")).doesNotValidate(model);
        assertThat(accountLogin.noneMatch("toto", "tutu")).validates(model);

        account.setCountry(Country.FR);
        assertThat(accountCountry.noneMatch(EnumSet.of(Country.CAN, Country.FR))).doesNotValidate(model);
        assertThat(accountCountry.noneMatch(EnumSet.of(Country.CAN, Country.US))).validates(model);

        assertThat(accountCountry.noneMatch(country -> country.name().equals("CAN"))).validates(model);
    }

}
