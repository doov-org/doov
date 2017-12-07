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
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.sample.field.SampleFieldIdInfo.userFirstName;
import static io.doov.sample.field.SampleFieldIdInfo.userLastName;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.*;

public class NaryTest {

    private SampleModelWrapper model;

    @BeforeEach
    public void before() {
        User user = new User();
        user.setFirstName("first name");
        user.setLastName("last name");

        SampleModel sampleModel = new SampleModel();
        sampleModel.setUser(user);

        model = new SampleModelWrapper(sampleModel);
    }

    @Test
    public void should_short_circuit_enabled_by_default() {
        ValidationRule rule;
        StepCondition node;

        rule = DOOV.when(matchAny(userFirstName().isNotNull(), userLastName().isNull())).validate();
        assertThat(rule).validates(model).hasFailedNodeEmpty();

        rule = DOOV.when(matchAny(userFirstName().isNotNull(), node = userLastName().isNull())).validate();
        rule = rule.withShortCircuit(false);
        assertThat(rule).validates(model).hasFailedNode(node.getMetadata());
    }

}
