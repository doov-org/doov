package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.impl.DefaultRuleRegistry.REGISTRY_DEFAULT;
import static io.doov.sample.validation.RulesConference.userAccount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.ast.AstFullVisitor;
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
        assertThat(result).isFalse().hasFailureCause("account phone number starts with '+33'");
    }

    @Test
    public void print_rules() {
        REGISTRY_DEFAULT.stream()
                .map(Readable::readable)
                .forEach(System.out::print);

        StringBuilder sb = new StringBuilder();
        REGISTRY_DEFAULT.stream()
                .peek(rule -> sb.append("--------------------------------").append("\n"))
                .forEach(rule -> rule.accept(new AstFullVisitor(sb), 0));
        System.out.println(sb.toString());
    }

}