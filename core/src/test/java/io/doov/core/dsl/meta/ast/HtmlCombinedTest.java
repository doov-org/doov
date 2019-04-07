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

import static io.doov.assertions.renderer.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;

public class HtmlCombinedTest {
    private StepCondition A, B, C;
    private Result result;
    private Document doc;
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
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(1);
        assertThat(doc).leaf_LI().hasSize(3);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("0 %", "100 %", "0 %", "0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always true", "always false", "always false");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B", "C");
        assertThat(doc).tokenNary_SPAN().containsExactly("match all");

    }

    @Test
    void reduce_and() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(1);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always true", "and", "always false");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B");
    }

    @Test
    void reduce_zeroInt() {
        result = when(zeroField.notEq(0)).validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("!=");
        assertThat(doc).tokenValue_SPAN().containsExactly("0");
        assertThat(doc).tokenField_SPAN().containsExactly("zero");
    }

    @Test
    void reduce_list() {
        result = when(iterableField.contains("c")).validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("contains");
        assertThat(doc).tokenValue_SPAN().containsExactly("'c'");
        assertThat(doc).tokenField_SPAN().containsExactly("list");
    }

    @Test
    void reduce_null() {
        result = when(enumField.isNull()).validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("is null");
        assertThat(doc).tokenField_SPAN().containsExactly("enum");
    }

    @Test
    void matches_regexp() {
        result = when(stringField.matches("^some.*")
                .or(stringField2.matches("^other.*"))).validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(1);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("matches", "or", "matches");
        assertThat(doc).tokenField_SPAN().containsExactly("string field 1", "string field 2");
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }
}
