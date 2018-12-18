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
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

public class ReductionTest {

    private static final Locale LOCALE = Locale.US;

    private StepCondition A, B, C;
    private ValidationRule rule;
    private Result result;
    private Metadata reduce;
    private GenericModel model;
    private IntegerFieldInfo zeroField;
    private IterableFieldInfo<String, List<String>> iterableField;
    private EnumFieldInfo<?> enumField;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.zeroField = model.intField(0, "zero");
        this.iterableField = model.iterableField(asList("a", "b"), "list");
        this.enumField = model.enumField(null, "enum");
    }

    @Test
    void reduce_matchAll() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        rule = when(matchAll(A, B, C)).validate().withShortCircuit(false);
        result = rule.execute();
        reduce = result.reduce(FAILURE);

        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when match all [always true A, always false B, always false C] validate");
        assertFalse(result.value());
        assertThat(collectMetadata(reduce)).contains(B.metadata());
        assertThat(collectMetadata(reduce)).contains(C.metadata());
    }

    @Test
    void reduce_and() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        rule = when(A.and(B)).validate().withShortCircuit(false);
        result = rule.execute();
        reduce = result.reduce(FAILURE);

        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when (always true A and always false B) validate");
        assertFalse(result.value());
        assertThat(collectMetadata(reduce)).contains(B.metadata());
    }

    @Test
    void reduce_zeroInt() {
        rule = when(zeroField.notEq(0)).validate().withShortCircuit(false);
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when zero != 0 validate");
        assertFalse(result.value());
    }

    @Test
    void reduce_list() {
        rule = when(iterableField.contains("c")).validate().withShortCircuit(false);
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when list contains 'c' validate");
        assertFalse(result.value());
    }

    @Test
    void reduce_null() {
        rule = when(enumField.isNull()).validate().withShortCircuit(false);
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when enum is null validate");
        assertTrue(result.value());
    }

    @AfterEach
    void afterEach() {
        System.out.println("> " + reduce);
    }
}
