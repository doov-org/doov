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
package io.doov.core.dsl.meta.predicate;

import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.SUCCESS;
import static io.doov.core.dsl.meta.predicate.MockConditions.alwaysFalse;
import static io.doov.core.dsl.meta.predicate.MockConditions.alwaysTrue;
import static io.doov.core.dsl.meta.predicate.MockConditions.collectMetadata;
import static io.doov.core.dsl.meta.predicate.MockConditions.reset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.EmptyMetadata;
import io.doov.core.dsl.meta.Metadata;

public class ReduceSuccessMatchAllTest {
    private StepCondition A, B, C;
    private Result result;
    private Metadata reduce;

    @Test
    void matchAll_true_true_true_success() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(SUCCESS);

        assertTrue(result.isTrue());
        assertThat(reduce).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(collectMetadata(reduce)).contains(A.metadata(), B.metadata(), C.metadata());
    }

    @Test
    void matchAll_true_true_false_success() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        C = alwaysFalse("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(SUCCESS);

        assertFalse(result.isTrue());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    void matchAll_true_false_false_success() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(SUCCESS);

        assertFalse(result.isTrue());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    void matchAll_false_false_false_success() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(SUCCESS);

        assertFalse(result.isTrue());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    @AfterEach
    void afterEach() {
        System.out.println("> " + reduce);
        reset();
    }
}
