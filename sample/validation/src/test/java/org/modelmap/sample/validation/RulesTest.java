package org.modelmap.sample.validation;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.modelmap.sample.validation.Rules.VALID_COUNTRY;
import static org.modelmap.sample.validation.Rules.VALID_EMAIL;
import static org.modelmap.sample.validation.Rules.rules;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.Result;
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
        System.out.println(VALID_EMAIL.readable());
        assertThat(VALID_EMAIL.executeOn(wrapper).isValid()).isTrue();

        account.setEmail("test@test.gh");
        assertThat(VALID_EMAIL.executeOn(wrapper).isValid()).isFalse();
    }

    @Test
    public void test_valid_country() {
        System.out.println(VALID_COUNTRY.readable());
        assertThat(VALID_COUNTRY.executeOn(wrapper).isValid()).isTrue();

        account.setPhoneNumber("+336123456789");
        assertThat(VALID_COUNTRY.executeOn(wrapper).isValid()).isFalse();
    }

    @Test
    public void test_all_rules_invalid_messages() {
        List<String> messages = rules.stream()
                        .map(rule -> rule.executeOn(wrapper))
                        .filter(Result::isInvalid)
                        .map(Result::getMessage)
                        .collect(toList());
        assertThat(messages).isEmpty();
    }

}
