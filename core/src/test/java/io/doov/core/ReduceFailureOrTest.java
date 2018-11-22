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
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.EmptyMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;

public class ReduceFailureOrTest {

    private StepCondition conditionA;
    private StepCondition conditionB;

    @Test
    void or_false_false_failure() {
        conditionA = falseCondition("A");
        conditionB = falseCondition("B");
        ValidationRule rule = DOOV.when(conditionA.or(conditionB)).validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is failure
        assertThat(result.isFalse()).isTrue();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reduce.readable());
        assertThat(collectMetadata(reduce)).contains(conditionA.metadata(), conditionB.metadata());
    }

    @Test
    void or_false_false_complex_failure() {
        conditionA = falseCondition("A");
        conditionB = falseCondition("B");
        StepCondition conditionC = trueCondition("C");
        ValidationRule rule = DOOV.when(conditionA.or(conditionB.and(conditionC))).validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is failure
        assertThat(result.isFalse()).isTrue();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reduce.readable());
        assertThat(reduce).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(collectMetadata(reduce)).contains(conditionA.metadata(), conditionB.metadata());
    }

    // TODO should return empty
    @Disabled
    @Test
    void or_true_false_failure() {
        conditionA = trueCondition("A");
        conditionB = falseCondition("B");
        ValidationRule rule = DOOV.when(conditionA.or(conditionB)).validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is not failure
        assertThat(result.isFalse()).isFalse();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reduce.readable());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    // TODO should return empty
    @Disabled
    @Test
    void or_false_true_failure() {
        conditionA = falseCondition("A");
        conditionB = trueCondition("B");
        ValidationRule rule = DOOV.when(conditionA.or(conditionB)).validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is not failure
        assertThat(result.isFalse()).isFalse();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reduce.readable());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    // TODO should return empty
    @Disabled
    @Test
    void or_true_true_failure() {
        conditionA = trueCondition("A");
        conditionB = trueCondition("B");
        ValidationRule rule = DOOV.when(conditionA.or(conditionB)).validate().withShortCircuit(false);
        Result result = rule.executeOn(null);
        // is not failure
        assertThat(result.isFalse()).isFalse();
        Metadata reduce = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reduce.readable());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }
}
