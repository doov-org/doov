/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToHtml;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.EnumTest.VAL1;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.EnumTest.VAL2;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.EnumTest.VAL3;
import static org.jsoup.Jsoup.parseBodyFragment;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.jupiter.api.*;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.EnumFieldInfo;
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
        return parseBodyFragment(astToHtml(result.getContext().getRootMetadata(), LOCALE));
    }

    static String format(Result result, Document doc) {
        return "<!-- " + AstVisitorUtils.astToString(result.getContext().getRootMetadata(), LOCALE) + " -->\n"
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
    }

    @Test
    void anyMatch_failure() {
        result = when(enumField.anyMatch(VAL2, VAL3)).validate().executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
    }

    @Test
    void and_combined_anyMatch_success() {
        A = DOOV.alwaysTrue("A");
        result = when(A.and(enumField.anyMatch(VAL1, VAL2, VAL3))).validate().executeOn(model);
        doc = documentOf(result);

        assertTrue(result.value());
    }

    @Test
    void and_combined_anyMatch_failure() {
        A = DOOV.alwaysTrue("A");
        result = when(A.and(enumField.anyMatch(VAL2, VAL3))).validate().executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
    }

    @Test
    void matchAny_combined_anyMatch_success() {
        A = DOOV.alwaysTrue("A");
        result = when(matchAny(A, enumField.anyMatch(VAL1, VAL2, VAL3))).validate().withShortCircuit(false)
                .executeOn(model);
        doc = documentOf(result);

        assertTrue(result.value());
    }

    @Test
    void matchAny_combined_anyMatch_failure() {
        A = DOOV.alwaysFalse("A");
        result = when(matchAny(A, enumField.anyMatch(VAL2, VAL3))).validate().executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }

    enum EnumTest {
        VAL1, VAL2, VAL3;
    }
}
