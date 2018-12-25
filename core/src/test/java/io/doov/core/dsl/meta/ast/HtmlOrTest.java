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
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import io.doov.core.dsl.field.types.BooleanFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.*;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;

public class HtmlOrTest {
    private StepCondition A, B, C, D;
    private Result result;
    private Document doc;
    GenericModel model = new GenericModel();
    private IntegerFieldInfo zero;
    private LocalDateFieldInfo yesterday;
    private StringFieldInfo bob;
    private BooleanFieldInfo is_true;

    @BeforeEach
    void beforeEach() {
        zero = model.intField(0, "zero");
        yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        bob = model.stringField("Bob", "name");
        is_true = model.booleanField(false, "is True");
    }

    @Test
    void or_true_false_complex() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        result = when(A.or(B.or(C))).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(2);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "0 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("always true", "always false", "always true");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("A", "B", "C");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("or", "or");
        assertThat(doc.select("ul.dsl-ul-binary")).extracting(Element::text)
                .containsExactly("0 % always false B or 100 % always true C");
    }

    @Test
    void or_false_true_complex() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        result = when(A.or(B.and(C))).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(2);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("0 %", "100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("always false", "always true", "always true");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("A", "B", "C");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("or", "and");
        assertThat(doc.select("ul.dsl-ul-binary")).extracting(Element::text)
                .containsExactly("100 % always true B and 100 % always true C");
    }

    @Test
    void or_false_false() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("0 %", "0 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("always false", "always false");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("A", "B");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("or");
    }

    @Test
    void or_false_false_complex() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        result = when(A.or(B.and(C))).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(2);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("0 %", "0 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("always false", "always false", "always true");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("A", "B", "C");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("or", "and");
        assertThat(doc.select("ul.dsl-ul-binary")).extracting(Element::text)
                .containsExactly("0 % always false B and 100 % always true C");
    }

    @Test
    void or_true_false() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "0 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("always true", "always false");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("A", "B");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("or");
    }

    @Test
    void or_false_true() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("0 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("always false", "always true");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("A", "B");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("or");
    }

    @Test
    void or_true_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("always true", "always true");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("A", "B");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("or");
    }

    @Test
    void or_or_or() {
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();
        result = when(A.or(B).or(C).or(D)).validate()
                .withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "100 %", "100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("<", "before", "today", "starts with", "is");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("zero", "yesterday", "name", "is True");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("4", "'B'", "false");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("or", "or", "or");
    }

    @Test
    void or_field_true_true() {
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        result = when(A.or(B)).validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("<", "before", "today");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("zero", "yesterday");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("4");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("or");
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }
}
