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
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.count;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.core.dsl.lang.ReduceType.SUCCESS;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.collectMetadata;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;

public class CountFunctionTest {

    private static Locale LOCALE = Locale.US;

    private StepCondition A, B, C;
    private Result result;
    private Metadata reduce;
    private ValidationRule rule;

    @Test
    void reduce_count_is_false() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        rule = when(count(A, B, C).greaterThan(1)).validate().withShortCircuit(false);
        result = rule.execute();
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when (count [always true A, always false B, always false C] > 1) validate");

        assertThat(reduce.readable(LOCALE)).isEqualTo("always false B and always false C");

        assertThat(collectMetadata(reduce)).doesNotContain(A.metadata());
        assertThat(collectMetadata(reduce)).contains(B.metadata());
        assertThat(collectMetadata(reduce)).contains(C.metadata());
    }

    @Test
    void reduce_count_is_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        C = alwaysFalse("C");
        rule = when(count(A, B, C).greaterOrEquals(1)).validate().withShortCircuit(false);
        result = rule.execute();
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when (count [always true A, always true B, always false C] >= 1) validate");

        assertThat(reduce.readable(LOCALE)).isEqualTo("always true A and always true B");

        assertThat(collectMetadata(reduce)).contains(A.metadata());
        assertThat(collectMetadata(reduce)).contains(B.metadata());
        assertThat(collectMetadata(reduce)).doesNotContain(C.metadata());
    }

    @AfterEach
    void afterEach() {
        System.out.println("> " + reduce);
    }
}
