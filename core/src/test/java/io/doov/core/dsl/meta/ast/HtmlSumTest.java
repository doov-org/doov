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
import static io.doov.core.dsl.DOOV.sum;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jsoup.nodes.Document;
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
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(2);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly(">");
        assertThat(doc).tokenValue_SPAN().containsExactly("1");
        assertThat(doc).tokenField_SPAN().containsExactly("A", "B");
        assertThat(doc).tokenNary_SPAN().containsExactly("sum");

    }

    @Test
    void sum_1_2_greaterThan_3() {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        result = when(sum(A, B).greaterThan(3)).validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(2);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly(">");
        assertThat(doc).tokenValue_SPAN().containsExactly("3");
        assertThat(doc).tokenField_SPAN().containsExactly("A", "B");
        assertThat(doc).tokenNary_SPAN().containsExactly("sum");
    }

    @Test
    void sum_sum_1_sum_2_greaterThan_3() {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        result = when(sum(sum(A), sum(B)).greaterThan(3)).validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(3);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(2);
        assertThat(doc).leaf_LI().hasSize(2);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly(">");
        assertThat(doc).tokenValue_SPAN().containsExactly("3");
        assertThat(doc).tokenField_SPAN().containsExactly("A", "B");
        assertThat(doc).tokenNary_SPAN().containsExactly("sum", "sum", "sum");
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }
}
