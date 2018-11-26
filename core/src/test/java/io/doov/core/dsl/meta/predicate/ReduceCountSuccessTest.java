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

import static io.doov.core.dsl.DOOV.*;
import static io.doov.core.dsl.lang.ReduceType.SUCCESS;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.collectMetadata;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.EmptyMetadata;
import io.doov.core.dsl.meta.Metadata;

public class ReduceCountSuccessTest {
    private StepCondition A, B;
    private Result result;
    private Metadata reduce;

    @Test
    void count_true_true_succes() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        result = when(count(A, B).greaterThan(1)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(collectMetadata(reduce)).contains(A.metadata(), B.metadata());
    }

    @Test
    void count_true_false_success() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(count(A, B).greaterOrEquals(1)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(reduce).isEqualTo(A.metadata());
    }

    @Test
    void count_false_false_success() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        result = when(count(A, B).greaterOrEquals(1)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(SUCCESS);

        assertFalse(result.value());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    @AfterEach
    void afterEach() {
        System.out.println("> " + reduce);
    }
}
