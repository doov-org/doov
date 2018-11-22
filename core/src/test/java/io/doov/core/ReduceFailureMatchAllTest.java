/*
 * Copyright 2018 Courtanet
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
package io.doov.core;

import static io.doov.core.MockConditions.collectMetadata;
import static io.doov.core.MockConditions.falseCondition;
import static io.doov.core.MockConditions.trueCondition;
import static io.doov.core.dsl.DOOV.matchAll;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.EmptyMetadata;
import io.doov.core.dsl.meta.Metadata;

public class ReduceFailureMatchAllTest {

    private StepCondition conditionA;
    private StepCondition conditionB;
    private StepCondition conditionC;

    @Test
    void matchAll_true_true_true_failure() {
        conditionA = trueCondition("A");
        conditionB = trueCondition("B");
        conditionC = trueCondition("C");
        ValidationRule rule =
                DOOV.when(matchAll(conditionA, conditionB, conditionC)).validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is not failure
        assertThat(result.isFalse()).isFalse();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reduce.readable());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    void matchAll_true_true_false_failure() {
        conditionA = trueCondition("A");
        conditionB = trueCondition("B");
        conditionC = falseCondition("C");
        ValidationRule rule =
                DOOV.when(matchAll(conditionA, conditionB, conditionC)).validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is failure
        assertThat(result.isFalse()).isTrue();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reduce.readable());
        assertThat(collectMetadata(reduce)).containsExactly(conditionC.metadata());
    }

    @Test
    void matchAll_true_false_false_failure() {
        conditionA = trueCondition("A");
        conditionB = falseCondition("B");
        conditionC = falseCondition("C");
        ValidationRule rule =
                DOOV.when(matchAll(conditionA, conditionB, conditionC)).validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is failure
        assertThat(result.isFalse()).isTrue();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reduce.readable());
        assertThat(collectMetadata(reduce))
                .contains(conditionB.metadata(), conditionC.metadata())
                .doesNotContain(conditionA.metadata());
    }

    @Test
    void matchAll_false_false_false_failure() {
        conditionA = falseCondition("A");
        conditionB = falseCondition("B");
        conditionC = falseCondition("C");
        ValidationRule rule =
                DOOV.when(matchAll(conditionA, conditionB, conditionC)).validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is failure
        assertThat(result.isFalse()).isTrue();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reduce.readable());
        assertThat(collectMetadata(reduce))
                .contains(conditionA.metadata(), conditionB.metadata(), conditionC.metadata());
    }
}
