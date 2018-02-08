/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.sample.validation.dsl;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.impl.DefaultRuleRegistry.REGISTRY_DEFAULT;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.*;
import io.doov.sample.validation.SampleRules;

public class RulesTest {

    private FieldModel model;

    @BeforeEach
    public void before() {
        model = new SampleModelWrapper(SampleModels.sample());
    }

    @MethodSource("rules")
    @ParameterizedTest
    public void should_rules_validates_with_no_messages(ValidationRule rule) {
        assertThat(rule).validates(model).hasNoFailureCause();
    }

    @MethodSource("rules")
    @ParameterizedTest
    public void should_rules_no_exception_on_null_model(ValidationRule rule) {
        rule.executeOn(new SampleModelWrapper());
        rule.executeOn(new SampleModelWrapper(new SampleModel()));
    }

    @SuppressWarnings("unused")
    private static Stream<ValidationRule> rules() {
        new SampleRules();
        return REGISTRY_DEFAULT.stream();
    }
}
