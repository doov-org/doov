package org.modelmap.sample.validation;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
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
    public void test_all_rules_invalid_messages() {
        account.setEmail("test@test.gh");

        List<String> messages = rules.stream()
                        .map(rule -> rule.executeOn(wrapper))
                        .filter(Result::isInvalid)
                        .map(Result::message)
                        .collect(toList());
        assertThat(messages).containsOnly("email finishes with .com or .fr");
    }

}
