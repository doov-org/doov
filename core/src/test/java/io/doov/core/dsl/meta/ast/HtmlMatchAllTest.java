/*
 * Copyright 2018 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;

public class HtmlMatchAllTest {
    private StepCondition A, B, C;
    private Result result;
    private Document doc;

    @Test
    void matchAll_true_true_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(1);
        assertThat(doc).leaf_LI().hasSize(3);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "100 %", "100 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always true", "always true", "always true");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B", "C");
        assertThat(doc).tokenNary_SPAN().containsExactly("match all");
    }

    @Test
    void matchAll_true_true_false() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
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
        assertThat(doc).percentageValue_DIV().containsExactly("0 %", "100 %", "100 %", "0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always true", "always true", "always false");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B", "C");
        assertThat(doc).tokenNary_SPAN().containsExactly("match all");
    }

    @Test
    void matchAll_true_false_false() {
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
    void matchAll_false_false_false() {
        A = alwaysFalse("A");
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
        assertThat(doc).percentageValue_DIV().containsExactly("0 %", "0 %", "0 %", "0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always false", "always false", "always false");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B", "C");
        assertThat(doc).tokenNary_SPAN().containsExactly("match all");
    }

    @Test
    void matchAll_field_false_false_false_failure() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo something = model.stringField("something", "string field");
        A = zero.greaterThan(4);
        B = yesterday.after(LocalDateSuppliers.today());
        C = something.matches("^other.*");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(3);
        assertThat(doc).nary_LI().hasSize(1);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("0 %", "0 %", "0 %", "0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly(">", "after", "today", "matches");
        assertThat(doc).tokenField_SPAN().containsExactly("zero", "yesterday", "string field");
        assertThat(doc).tokenValue_SPAN().containsExactly("4", "'^other.*'");
        assertThat(doc).tokenNary_SPAN().containsExactly("match all");
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }
}
