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
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.parse;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.render;
import static java.util.Arrays.asList;

import java.util.List;

import org.commonmark.node.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;

public class MarkdownCombinedTest {
    private StepCondition A, B, C;
    private ValidationRule rule;
    private Node node;
    private GenericModel model;
    private StringFieldInfo stringField;
    private StringFieldInfo stringField2;
    private IntegerFieldInfo zeroField;
    private IterableFieldInfo<String, List<String>> iterableField;
    private EnumFieldInfo<?> enumField;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.zeroField = model.intField(0, "zero");
        this.stringField = model.stringField("some string", "string field 1");
        this.stringField2 = model.stringField("other string", "string field 2");
        this.iterableField = model.iterableField(asList("a", "b"), "list");
        this.enumField = model.enumField(null, "enum");
    }

    @Test
    void reduce_matchAll() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        rule = when(matchAll(A, B, C)).validate();
        node = parse(rule.metadata());
    }

    @Test
    void reduce_and() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        rule = when(A.and(B)).validate();
        node = parse(rule.metadata());
    }

    @Test
    void reduce_zeroInt() {
        rule = when(zeroField.notEq(0)).validate();
        node = parse(rule.metadata());
    }

    @Test
    void reduce_list() {
        rule = when(iterableField.contains("c")).validate();
        node = parse(rule.metadata());
    }

    @Test
    void reduce_null() {
        rule = when(enumField.isNull()).validate();
        node = parse(rule.metadata());
    }

    @Test
    void matches_regexp() {
        rule = when(stringField.matches("^some.*")
                .or(stringField2.matches("^other.*"))).validate();
        node = parse(rule.metadata());
    }

    @AfterEach
    void afterEach() {
        System.out.println(render(node));
    }
}
