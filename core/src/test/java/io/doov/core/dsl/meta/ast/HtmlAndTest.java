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
import static io.doov.core.dsl.DOOV.count;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.*;

import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;

public class HtmlAndTest {
    private StepCondition A, B, C, D;
    private Result result;
    private Document doc;

    @Test
    void and_false_false() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
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
                .containsExactly("and");
    }

    @Test
    void and_true_false() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
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
                .containsExactly("100 %", "0 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("always true", "always false");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("A", "B");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("and");
    }

    @Test
    void and_false_true() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
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
                .containsExactly("0 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("always false", "always true");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("A", "B");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("and");
    }

    @Test
    void and_true_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
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
                .containsExactly("and");
    }

    @Test
    @Disabled
    // FIXME broken since leaf metadata refactoring
    void and_field_true_true_failure() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());

        result = when(A.and(B)).validate().withShortCircuit(false).executeOn(model);
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
                .containsExactly("and");
    }
    
    @Test
    void and_and_and() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();

        result = when(A.and(B)
                .and(C)
                .and(D)).validate().withShortCircuit(false).executeOn(model);
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
                .containsExactly("and", "and", "and");
    }

    @Test
    void and_and_count() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();

        result = when(A.and(B
                        .and(count(C, D).greaterThan(1))))
                .validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(2);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(2);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "100 %", "100 %", "100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("<", "before", "today", "starts with", "is");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("zero", "yesterday", "name", "is True");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("4", "'B'", "false", "1");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("and", "and", ">");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("count");
    }

    @Test
    void and_or_and() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();

        result = when(A.and(B)
                .or(C.and(D)))
                .validate().withShortCircuit(false).executeOn(model);
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
                .containsExactly("100 %", "100 %", "100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("<", "before", "today", "starts with", "is");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("zero", "yesterday", "name", "is True");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("4", "'B'", "false");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("and", "or", "and");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .isEmpty();
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }
}
