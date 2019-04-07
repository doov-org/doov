/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.assertions.renderer.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.parse;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.render;
import static io.doov.core.dsl.meta.ast.MarkdownAnyMatchTest.EnumTest.VAL1;
import static io.doov.core.dsl.meta.ast.MarkdownAnyMatchTest.EnumTest.VAL2;
import static io.doov.core.dsl.meta.ast.MarkdownAnyMatchTest.EnumTest.VAL3;

import org.commonmark.node.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;

public class MarkdownAnyMatchTest {
    private StepCondition A;
    private ValidationRule rule;
    private Node node;
    private GenericModel model;
    private EnumFieldInfo<EnumTest> enumField;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.enumField = model.enumField(VAL1, "enumField");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void anyMatch_success() {
        rule = when(enumField.anyMatch(VAL1, VAL2, VAL3)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "enumField match any  : VAL1, VAL2, VAL3",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void anyMatch_failure() {
        rule = when(enumField.anyMatch(VAL2, VAL3)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "enumField match any  : VAL2, VAL3",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void and_combined_anyMatch_success() {
        A = DOOV.alwaysTrue("A");
        rule = when(A.and(enumField.anyMatch(VAL1, VAL2, VAL3))).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(5);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(5);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "always true A and",
                "enumField match any  : VAL1, VAL2, VAL3",
                "validate");
    }

    @Test
    void and_combined_anyMatch_failure() {
        A = DOOV.alwaysTrue("A");
        rule = when(A.and(enumField.anyMatch(VAL2, VAL3))).validate();
        node = parse(rule.metadata());
    }

    @Test
    @Disabled
    // FIXME Markdown
    void matchAny_combined_anyMatch_success() {
        A = DOOV.alwaysTrue("A");
        rule = when(matchAny(A, enumField.anyMatch(VAL1, VAL2, VAL3))).validate().withShortCircuit(false);
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(6);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(6);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "match any",
                "always true A",
                "enumField match any  : VAL1, VAL2, VAL3",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void matchAny_combined_anyMatch_failure() {
        A = DOOV.alwaysFalse("A");
        rule = when(matchAny(A, enumField.anyMatch(VAL2, VAL3))).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(6);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(6);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "match any",
                "always false A",
                "enumField match any  : VAL2, VAL3",
                "validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(render(node));
    }

    enum EnumTest {
        VAL1, VAL2, VAL3;
    }
}
