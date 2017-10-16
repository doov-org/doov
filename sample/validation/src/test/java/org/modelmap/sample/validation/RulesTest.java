package org.modelmap.sample.validation;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.modelmap.sample.validation.AccountRulesId.VALID_COUNTRY;
import static org.modelmap.sample.validation.AccountRulesId.VALID_EMAIL;
import static org.modelmap.sample.validation.AccountRulesId.VALID_EMAIL_SIZE;
import static org.modelmap.sample.validation.Rules.REGISTRY_ACCOUNT;
import static org.modelmap.sample.validation.Rules.REGISTRY_USER;

import java.util.List;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.*;
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
        assertThat(executeOn(REGISTRY_ACCOUNT, VALID_EMAIL).isValid()).isTrue();

        account.setEmail("test@test.gh");
        assertThat(executeOn(REGISTRY_ACCOUNT, VALID_EMAIL).isValid()).isFalse();
    }

    @Test
    public void test_valid_email_size() {
        assertThat(executeOn(REGISTRY_ACCOUNT, VALID_EMAIL_SIZE).isValid()).isTrue();

        account.setMaxEmailSize(5);
        assertThat(executeOn(REGISTRY_ACCOUNT, VALID_EMAIL_SIZE).isValid()).isFalse();
    }

    @Test
    public void test_valid_country() {
        assertThat(executeOn(REGISTRY_ACCOUNT, VALID_COUNTRY).isValid()).isTrue();

        account.setPhoneNumber("+336123456789");
        assertThat(executeOn(REGISTRY_ACCOUNT, VALID_COUNTRY).isValid()).isFalse();
    }

    @Test
    public void test_all_account_rules_invalid_messages() {
        List<Result> messages = REGISTRY_ACCOUNT.stream()
                        .map(rule -> rule.executeOn(wrapper))
                        .collect(toList());
        assertThat(messages).isNotEmpty();
        assertThat(messages).extracting(Result::isValid).allMatch(Boolean::booleanValue);
        assertThat(messages).extracting(Result::getMessage).allMatch(Objects::isNull);
    }

    @Test
    public void print_all_account_rules() {
        concat(REGISTRY_USER.stream(), REGISTRY_ACCOUNT.stream())
                        .map(ValidationRule::readable)
                        .forEach(System.out::println);
    }

    private Result executeOn(RuleRegistry registry, AccountRulesId id) {
        return registry.get(id).map(rule -> rule.executeOn(wrapper)).orElseThrow(AssertionError::new);
    }

}
