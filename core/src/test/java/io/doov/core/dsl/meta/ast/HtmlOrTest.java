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
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.BooleanFieldInfo;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;

public class HtmlOrTest {
    private StepCondition A, B, C, D;
    private Result result;
    private Document doc;

    @Test
    void or_true_false_complex() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        result = when(A.or(B.or(C))).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(1);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().isEmpty();
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "0 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always true", "or", "always false", "or", "always true");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B", "C");
        assertThat(doc).binary_LI().extracting(Element::text)
                .containsExactly("100 % always true A or 0 % always false B or 100 % always true C");
    }

    @Test
    void or_false_true_complex() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        result = when(A.or(B.and(C))).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(2);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(1);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("0 %", "100 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always false", "or", "always true", "and", "always true");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B", "C");
        assertThat(doc).binary_UL().extracting(Element::text)
                .containsExactly("100 % always true B and 100 % always true C");
    }

    @Test
    void or_false_false() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
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
        assertThat(doc).percentageValue_DIV().containsExactly("0 %", "0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always false", "or", "always false");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B");
    }

    @Test
    void or_false_false_complex() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        result = when(A.or(B.and(C))).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(2);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(1);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("0 %", "0 %", "100 %");
        assertThat(doc).tokenOperator_SPAN()
                .containsExactly("always false", "or", "always false", "and", "always true");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B", "C");
        assertThat(doc).binary_UL().extracting(Element::text)
                .containsExactly("0 % always false B and 100 % always true C");
    }

    @Test
    void or_true_false() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
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
        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always true", "or", "always false");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B");
    }

    @Test
    void or_false_true() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
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
        assertThat(doc).percentageValue_DIV().containsExactly("0 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always false", "or", "always true");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B");
    }

    @Test
    void or_true_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("always true", "or", "always true");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", "B");
    }

    @Test
    void or_field_true_true() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        result = when(A.or(B)).validate().withShortCircuit(false).executeOn(model);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("<", "or", "before", "today");
        assertThat(doc).tokenField_SPAN().containsExactly("zero", "yesterday");
        assertThat(doc).tokenValue_SPAN().containsExactly("4");
    }

    @Test
    void or_or_or() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();

        result = when(A.or(B)
                .or(C)
                .or(D)).validate().withShortCircuit(false).executeOn(model);
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
        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "100 %", "100 %", "100 %");
        assertThat(doc).tokenOperator_SPAN()
                .containsExactly("<", "or", "before", "today", "or", "starts with", "or", "is");
        assertThat(doc).tokenField_SPAN().containsExactly("zero", "yesterday", "name", "is True");
        assertThat(doc).tokenValue_SPAN().containsExactly("4", "'B'", "false");
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }
}
