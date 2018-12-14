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

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.EmptyMetadata;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;

public class ReduceSuccessMatchAnyTest {

    private static final Locale LOCALE = Locale.US;

    private StepCondition A, B, C, D;
    private Result result;
    private Metadata reduce;

    @Test
    void matchAny_false_false_false_success() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        ValidationRule rule = when(matchAny(A, B, C)).validate().withShortCircuit(false);
        result = rule.execute();
        reduce = result.reduce(SUCCESS);

        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when match any [always false A, always false B, always false C] validate");

        assertFalse(result.value());
        assertThat(reduce).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    void matchAny_true_false_false_success() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        result = when(matchAny(A, B, C)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(reduce).isEqualTo(A.metadata());
    }

    @Test
    void matchAny_true_false_false_complex_success() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        D = alwaysFalse("D");
        ValidationRule rule = when(matchAny(A.or(D), B, C)).validate().withShortCircuit(false);
        result = rule.execute();
        reduce = result.reduce(SUCCESS);

        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when match any [(always true A or always false D), always false B, always false C] " +
                        "validate");

        assertTrue(result.value());
        assertThat(reduce).isInstanceOf(LeafMetadata.class);
        assertThat(reduce).isEqualTo(A.metadata());
    }

    @Test
    void matchAny_false_true_true_success() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        result = when(matchAny(A, B, C)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(reduce).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(collectMetadata(reduce)).contains(B.metadata(), C.metadata());
    }

    @Test
    void matchAny_false_true_true_complex_success() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        D = alwaysTrue("D");
        ValidationRule rule = when(matchAny(A, B, C.and(D))).validate().withShortCircuit(false);
        result = rule.execute();
        reduce = result.reduce(SUCCESS);

        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when match any [always false A, always true B, (always true C and always true D)] " +
                        "validate");

        assertTrue(result.value());
        assertThat(reduce).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(collectMetadata(reduce)).contains(B.metadata(), C.metadata(), D.metadata());
    }

    @Test
    void matchAny_true_true_true_success() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        result = when(matchAny(A, B, C)).validate().withShortCircuit(false).execute();
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(reduce).isNotEqualTo(result.getContext().getRootMetadata());
        assertThat(collectMetadata(reduce)).contains(A.metadata(), B.metadata(), C.metadata());
    }

    @AfterEach
    void afterEach() {
        System.out.println("> " + reduce);
    }
}
