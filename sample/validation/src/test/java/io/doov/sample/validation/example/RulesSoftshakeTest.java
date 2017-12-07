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
package io.doov.sample.validation.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Result;
import io.doov.sample.model.SampleModels;

public class RulesSoftshakeTest {

    @Test
    public void test() {
        // Given
        FieldModel wrapper = SampleModels.wrapper();

        // When
        Result result = RulesSoftshake.EXAMPLE.executeOn(wrapper);

        // Then
        assertThat(result.isTrue()).isTrue();
    }

}
