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
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.collectMetadata;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.EmptyMetadata;
import io.doov.core.dsl.meta.Metadata;

public class ReduceFailureOrTest {

    private static final Locale LOCALE = Locale.US;

    private StepCondition A, B, C;
    private Result result;
    private Metadata reduce;

    @Test
    void or_false_false_failure() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        ValidationRule rule = when(A.or(B)).validate().withShortCircuit(false);
        result = rule.execute();
        reduce = result.reduce(FAILURE);

        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when always false A or always false B validate");

        assertFalse(result.value());
        assertThat(collectMetadata(reduce)).contains(A.metadata(), B.metadata());
    }

    @Test
    void or_false_false_complex_failure() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        ValidationRule rule = when(A.or(B.and(C))).validate().withShortCircuit(false);
        result = rule.execute();
        reduce = result.reduce(FAILURE);

        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when always false A or (always false B and always true C) validate");

        assertFalse(result.value());
        assertThat(reduce).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(collectMetadata(reduce)).contains(A.metadata(), B.metadata());
    }

    @Test
    void or_true_false_failure() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(FAILURE);

        assertTrue(result.value());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    void or_false_true_failure() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(FAILURE);

        assertTrue(result.value());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    void or_true_true_failure() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(FAILURE);

        assertTrue(result.value());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    @AfterEach
    void afterEach() {
        System.out.println("> " + reduce);
    }
}
