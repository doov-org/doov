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
import static io.doov.core.dsl.DOOV.count;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.EmptyMetadata;
import io.doov.core.dsl.meta.Metadata;

public class ReduceCountSuccessTest {

    private StepCondition conditionA;
    private StepCondition conditionB;

    @Test
    void count_true_true_succes() {
        conditionA = trueCondition("A");
        conditionB = trueCondition("B");
        ValidationRule rule = DOOV.when(count(conditionA, conditionB).greaterThan(1))
                .validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is success
        assertThat(result.isTrue()).isTrue();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.SUCCESS);
        System.out.println(reduce.readable());
        assertThat(collectMetadata(reduce)).contains(conditionA.metadata(), conditionB.metadata());
    }

    @Test
    void count_true_false_success() {
        conditionA = trueCondition("A");
        conditionB = falseCondition("B");
        ValidationRule rule = DOOV.when(count(conditionA, conditionB).greaterOrEquals(1))
                .validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is success
        assertThat(result.isTrue()).isTrue();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.SUCCESS);
        System.out.println(reduce.readable());
        assertThat(reduce).isEqualTo(conditionA.metadata());
    }

    @Test
    void count_false_false_success() {
        conditionA = falseCondition("A");
        conditionB = falseCondition("B");
        ValidationRule rule = DOOV.when(count(conditionA, conditionB).greaterOrEquals(1))
                .validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is success
        assertThat(result.isTrue()).isFalse();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.SUCCESS);
        System.out.println(reduce.readable());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }
}
