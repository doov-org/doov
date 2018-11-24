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
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.core.dsl.meta.predicate.MockConditions.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;

public class ReductionTest {
    private StepCondition A, B, C;
    private Result result;
    private Metadata reduce;

    @Test
    void reduce_matchAll() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(FAILURE);

        assertFalse(result.isTrue());
        assertThat(collectMetadata(reduce)).contains(B.metadata());
        assertThat(collectMetadata(reduce)).contains(C.metadata());
    }

    @Test
    void reduce_and() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(FAILURE);

        assertFalse(result.isTrue());
        assertThat(collectMetadata(reduce)).contains(B.metadata());
    }

    @Test
    void reduce_zeroInt() {
        result = when(intField(0, "zero").notEq(0)).validate().withShortCircuit(false).executeOn(model());
        reduce = result.reduce(FAILURE);
        assertFalse(result.isTrue());
    }

    @Test
    void reduce_list() {
        result = when(iterableField(asList("a", "b"), "list").contains("c")).validate()
                .withShortCircuit(false).executeOn(model());
        reduce = result.reduce(FAILURE);
        assertFalse(result.isTrue());
    }

    @AfterEach
    void afterEach() {
        System.out.println("> " + reduce);
        reset();
    }
}
