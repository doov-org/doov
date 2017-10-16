package org.modelmap.sample.validation;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.modelmap.sample.validation.Registry.ACCOUNT_VALID_COUNTRY;
import static org.modelmap.sample.validation.Registry.ACCOUNT_VALID_EMAIL;

import java.util.List;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.Result;
import org.modelmap.core.dsl.lang.ValidationRule;
import org.modelmap.sample.model.*;

public class RulesTest {

    private FieldModel wrapper;
    private Account account;

    @Before
    public void before() {
        SampleModel sample = SampleModels.sample();
        wrapper = new SampleModelWrapper(sample);
        account = sample.getAccount();
    }

    @Test
    public void test_valid_email() {
        assertThat(ACCOUNT_VALID_EMAIL.executeOn(wrapper).isValid()).isTrue();

        account.setEmail("test@test.gh");
        assertThat(ACCOUNT_VALID_EMAIL.executeOn(wrapper).isValid()).isFalse();
    }

    @Test
    public void test_valid_email_size() {
        assertThat(ACCOUNT_VALID_EMAIL.executeOn(wrapper).isValid()).isTrue();

        account.setEmail("a@b.c");
        assertThat(ACCOUNT_VALID_EMAIL.executeOn(wrapper).isValid()).isFalse();
    }

    @Test
    public void test_valid_country() {
        assertThat(ACCOUNT_VALID_COUNTRY.executeOn(wrapper).isValid()).isTrue();

        account.setPhoneNumber("+336123456789");
        assertThat(ACCOUNT_VALID_COUNTRY.executeOn(wrapper).isValid()).isFalse();
        System.out.println(ACCOUNT_VALID_COUNTRY.readable());
    }

    @Test
    public void test_all_account_rules_invalid_messages() {
        List<Result> messages = Registry.ACCOUNT.stream()
                        .map(rule -> rule.executeOn(wrapper))
                        .collect(toList());
        assertThat(messages).isNotEmpty();
        assertThat(messages).extracting(Result::isValid).allMatch(Boolean::booleanValue);
        assertThat(messages).extracting(Result::getMessage).allMatch(Objects::isNull);
    }

    @Test
    public void print_all_account_rules() {
        Registry.ACCOUNT.stream()
                        .map(ValidationRule::readable)
                        .forEach(System.out::println);
    }

}
