/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.AstHtmlVisitor.astToHtml;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.parse;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.render;
import static io.doov.core.dsl.meta.ast.MarkdownAnyMatchTest.EnumTest.VAL1;
import static io.doov.core.dsl.meta.ast.MarkdownAnyMatchTest.EnumTest.VAL2;
import static io.doov.core.dsl.meta.ast.MarkdownAnyMatchTest.EnumTest.VAL3;
import static org.jsoup.Jsoup.parseBodyFragment;

import java.util.Locale;

import org.commonmark.node.Node;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;

public class MarkdownAnyMatchTest {
    private static final Locale LOCALE = Locale.US;
    private StepCondition A;
    private ValidationRule rule;
    private Node node;
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
        rule = when(enumField.anyMatch(VAL1, VAL2, VAL3)).validate();
        node = parse(rule.metadata());
    }

    @Test
    void anyMatch_failure() {
        rule = when(enumField.anyMatch(VAL2, VAL3)).validate();
        node = parse(rule.metadata());
    }

    @Test
    void and_combined_anyMatch_success() {
        A = DOOV.alwaysTrue("A");
        rule = when(A.and(enumField.anyMatch(VAL1, VAL2, VAL3))).validate();
        node = parse(rule.metadata());
    }

    @Test
    void and_combined_anyMatch_failure() {
        A = DOOV.alwaysTrue("A");
        rule = when(A.and(enumField.anyMatch(VAL2, VAL3))).validate();
        node = parse(rule.metadata());
    }

    @Test
    void matchAny_combined_anyMatch_success() {
        A = DOOV.alwaysTrue("A");
        rule = when(matchAny(A, enumField.anyMatch(VAL1, VAL2, VAL3))).validate().withShortCircuit(false)
                ;
        node = parse(rule.metadata());
    }

    @Test
    void matchAny_combined_anyMatch_failure() {
        A = DOOV.alwaysFalse("A");
        rule = when(matchAny(A, enumField.anyMatch(VAL2, VAL3))).validate();
        node = parse(rule.metadata());
    }

    @AfterEach
    void afterEach() {
        System.out.println(render(node));
    }

    enum EnumTest {
        VAL1, VAL2, VAL3;
    }
}
