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

import static io.doov.core.dsl.DOOV.sum;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.runtime.GenericModel;

public class HtmlSumTest {
    private final GenericModel model = new GenericModel();
    private IntegerFieldInfo A, B;
    private Result result;
    private Document doc;

    @Test
    void sum_1_2_greaterThan_1() {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        result = when(sum(A, B).greaterThan(1)).validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(2);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text).isEmpty();
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("1");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("A", "B");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("sum");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly(">");
    }

    @Test
    void sum_1_2_greaterThan_3() {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        result = when(sum(A, B).greaterThan(3)).validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(2);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("0 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text).isEmpty();
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("3");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("A", "B");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("sum");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly(">");
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }
}
