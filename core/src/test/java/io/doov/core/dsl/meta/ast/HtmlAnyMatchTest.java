/*
 * Copyright 2017 Courtanet
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
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.toHtml;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.EnumTest.VAL1;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.EnumTest.VAL2;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.EnumTest.VAL3;
import static org.jsoup.Jsoup.parseBodyFragment;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;

public class HtmlAnyMatchTest {
    private static final Locale LOCALE = Locale.US;
    private StepCondition A;
    private Result result;
    private Document doc;
    private GenericModel model;
    private EnumFieldInfo<EnumTest> enumField;

    static Document documentOf(Result result) {
        return documentOf(result.getContext());
    }

    static Document documentOf(Context context) {
        return parseBodyFragment(toHtml(context.getRootMetadata(), LOCALE));
    }

    static String format(Result result, Document doc) {
        return format(result.getContext(), doc);
    }

    static String format(Context context, Document doc) {
        return "<!-- " + AstVisitorUtils.astToString(context.getRootMetadata(), LOCALE) + " -->\n"
                + doc.outputSettings(new OutputSettings().prettyPrint(true).indentAmount(2)).toString();
    }

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.enumField = model.enumField(VAL1, "enumField");
    }

    @Test
    void anyMatch_success() {
        result = when(enumField.anyMatch(VAL1, VAL2, VAL3)).validate().executeOn(model);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("match any");
        assertThat(doc).tokenValue_SPAN().containsExactly(": VAL1, VAL2, VAL3");
        assertThat(doc).tokenField_SPAN().containsExactly("enumField");
    }

    @Test
    void anyMatch_failure() {
        result = when(enumField.anyMatch(VAL2, VAL3)).validate().executeOn(model);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("match any");
        assertThat(doc).tokenValue_SPAN().containsExactly(": VAL2, VAL3");
        assertThat(doc).tokenField_SPAN().containsExactly("enumField");
    }

    @Test
    void and_combined_anyMatch_success() {
        A = DOOV.alwaysTrue("A");
        result = when(A.and(enumField.anyMatch(VAL1, VAL2, VAL3))).validate().executeOn(model);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("always true", "and", "match any");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", ": VAL1, VAL2, VAL3");
        assertThat(doc).tokenField_SPAN().containsExactly("enumField");
    }

    @Test
    void and_combined_anyMatch_failure() {
        A = DOOV.alwaysTrue("A");
        result = when(A.and(enumField.anyMatch(VAL2, VAL3))).validate().executeOn(model);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("always true", "and", "match any");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", ": VAL2, VAL3");
        assertThat(doc).tokenField_SPAN().containsExactly("enumField");
    }

    @Test
    void matchAny_combined_anyMatch_success() {
        A = DOOV.alwaysTrue("A");
        result = when(matchAny(A, enumField.anyMatch(VAL1, VAL2, VAL3))).validate().withShortCircuit(false)
                .executeOn(model);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(1);
        assertThat(doc).nary_LI().hasSize(1);
        assertThat(doc).leaf_LI().hasSize(1);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "100 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always true", "match any");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", ": VAL1, VAL2, VAL3");
        assertThat(doc).tokenNary_SPAN().containsExactly("match any");
        assertThat(doc).tokenField_SPAN().containsExactly("enumField");
    }

    @Test
    void matchAny_combined_anyMatch_failure() {
        A = DOOV.alwaysFalse("A");
        result = when(matchAny(A, enumField.anyMatch(VAL2, VAL3))).validate().executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(1);
        assertThat(doc).nary_LI().hasSize(1);
        assertThat(doc).leaf_LI().hasSize(1);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).percentageValue_DIV().containsExactly("0 %", "0 %", "0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("always false", "match any");
        assertThat(doc).tokenValue_SPAN().containsExactly("A", ": VAL2, VAL3");
        assertThat(doc).tokenNary_SPAN().containsExactly("match any");
        assertThat(doc).tokenField_SPAN().containsExactly("enumField");
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }

    enum EnumTest {
        VAL1, VAL2, VAL3;
    }
}
