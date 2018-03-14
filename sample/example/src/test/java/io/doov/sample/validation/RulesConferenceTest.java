package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.impl.DefaultRuleRegistry.REGISTRY_DEFAULT;
import static io.doov.sample.field.dsl.DslSampleModel.accountEmail;
import static io.doov.sample.validation.RulesConference.userAccount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.ast.AstFullVisitor;
import io.doov.sample.model.SampleModels;

public class RulesConferenceTest {

    private FieldModel wrapper;

    @BeforeEach
    public void before() {
        wrapper = SampleModels.wrapper();
    }

    @Test
    public void should_user_account_validates() {
        // Condition assert
        assertThat(accountEmail.isNotNull()).validates(wrapper);

        // Rule assert
        assertThat(userAccount).validates(wrapper);

        // Result assert
        Result result = userAccount.executeOn(wrapper);
        assertThat(result).isTrue();
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