/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.sample.field.SampleFieldIdInfo.userFirstName;
import static io.doov.sample.validation.Rules.REGISTRY_ACCOUNT;
import static io.doov.sample.validation.Rules.REGISTRY_USER;
import static io.doov.sample.validation.id.AccountRulesId.VALID_ACCOUNT_01;
import static io.doov.sample.validation.id.AccountRulesId.VALID_EMAIL;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.*;
import io.doov.sample.model.*;

public class RulesTest {

    private FieldModel wrapper;
    private Account account;
    private User user;

    @Before
    public void before() {
        SampleModel sample = SampleModels.sample();
        wrapper = new SampleModelWrapper(sample);
        account = sample.getAccount();
        user = sample.getUser();
    }

    @Test
    public void test_valid_email() {
        assertThat(executeOn(REGISTRY_ACCOUNT, VALID_EMAIL).isTrue()).isTrue();

        account.setEmail("test@test.gh");
        Result actual = executeOn(REGISTRY_ACCOUNT, VALID_EMAIL);
        assertThat(actual.isTrue()).isFalse();
        assertThat(actual.getFailedNodes()).hasSize(3);
        assertThat(actual.getFailedNodes().stream().map(Readable::readable).collect(toList()))
                        .contains("account email matches '\\w+[@]\\w+\\.com' ",
                                        "account email matches '\\w+[@]\\w+\\.fr' ",
                                        "(account email matches '\\w+[@]\\w+\\.com' or " +
                                                        "account email matches '\\w+[@]\\w+\\.fr') ");
    }

    @Test
    public void test_valid_account() {
        assertThat(executeOn(REGISTRY_ACCOUNT, VALID_ACCOUNT_01).isTrue()).isTrue();

        account.setPhoneNumber("+446123456789");
        assertThat(executeOn(REGISTRY_ACCOUNT, VALID_ACCOUNT_01).isTrue()).isFalse();
    }

    @Test
    public void test_all_account_rules_invalid_messages() {
        List<Result> results = Stream.concat(REGISTRY_USER.stream(), REGISTRY_ACCOUNT.stream())
                        .map(rule -> rule.executeOn(wrapper))
                        .collect(toList());
        assertThat(results).isNotEmpty();
        assertThat(results).extracting(Result::isTrue).allMatch(Boolean::booleanValue);
        assertThat(results).extracting(Result::getMessage).allMatch(Objects::isNull);
    }

    @Test
    public void test_null() {
        assertThat(DOOV.when(userFirstName().isNull()).validate().executeOn(wrapper)).isFalse();
        assertThat(DOOV.when(userFirstName().isNotNull()).validate().executeOn(wrapper)).isTrue();
        user.setFirstName(null);
        assertThat(DOOV.when(userFirstName().isNull()).validate().executeOn(wrapper)).isTrue();
        assertThat(DOOV.when(userFirstName().isNotNull()).validate().executeOn(wrapper)).isFalse();
    }

    @Test
    public void print_all_account_rules() {
        concat(REGISTRY_USER.stream(), REGISTRY_ACCOUNT.stream())
                        .map(ValidationRule::readable)
                        .forEach(System.out::println);
    }

    private Result executeOn(RuleRegistry registry, RuleId id) {
        return registry.get(id).map(rule -> rule.executeOn(wrapper)).orElseThrow(AssertionError::new);
    }

}
