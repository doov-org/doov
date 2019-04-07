/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.ast;

import static io.doov.assertions.renderer.Assertions.assertThat;
import static io.doov.sample.validation.SampleRules.*;

import java.util.Locale;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.meta.Metadata;

public class MarkdownSampleRulesTest {
    private static final Locale LOCALE = Locale.US;
    private Node node;

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_EMAIL() {
        node = parse(RULE_EMAIL.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(5);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(5);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "account email matches '\\w+[@]\\w+.com' or",
                "account email matches '\\w+[@]\\w+.fr'",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_ACCOUNT() {
        node = parse(RULE_ACCOUNT.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(8);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(8);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "match all",
                "user birthdate age at today >= 18",
                "account email length is <= configuration max email size",
                "account country = FR and",
                "account phone number starts with '+33'",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_ACCOUNT_2() {
        node = parse(RULE_ACCOUNT_2.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(7);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "user birthdate age at today >= 18 and",
                "account email length is <= configuration max email size and",
                "account country = FR and",
                "account phone number starts with '+33'",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_USER() {
        node = parse(RULE_USER.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(8);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(8);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "count",
                "user first name is not null",
                "user last name is not null and",
                "user last name matches '[A-Z]+' >=",
                "0",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_USER_2() {
        node = parse(RULE_USER_2.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(9);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "user last name is not null and",
                "user last name matches '[A-Z]+' and",
                "count",
                "* account phone number is not null",
                "* account email is not null >",
                "0",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_USER_ADULT() {
        node = parse(RULE_USER_ADULT.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "user birthdate age at account creation date >= 18",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_USER_ADULT_FIRSTDAY() {
        node = parse(RULE_USER_ADULT_FIRSTDAY.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "user birthdate age at account creation date with first day of year >= 18",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_FIRST_NAME() {
        node = parse(RULE_FIRST_NAME.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(5);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(5);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "match all",
                "user first name as a number -function-  = 1",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_ID() {
        node = parse(RULE_ID.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "user id is not null",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_AGE() {
        node = parse(RULE_AGE.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "user birthdate age at today >= 18",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_AGE_2() {
        node = parse(RULE_AGE_2.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "user birthdate after user birthdate minus 1 day(s)",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_SUM() {
        node = parse(RULE_SUM.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(7);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "sum",
                "configuration min age x 0",
                "configuration max email size x 1 >=",
                "0",
                "validate");
    }

    @Test
    void RULE_MIN() {
        node = parse(RULE_MIN.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(7);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "min",
                "configuration min age",
                "configuration max email size >=",
                "0",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_DOUBLE_LAMBDA() {
        node = parse(RULE_DOUBLE_LAMBDA.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "favorite site name 1 match any -function- -function-",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_BORN_1980() {
        node = parse(RULE_BORN_1980.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "user birthdate as a number -function-  = 1980",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_ACCOUNT_TIME_CONTAINS() {
        node = parse(RULE_ACCOUNT_TIME_CONTAINS.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "account timezone as a string -function-  contains '00:00'",
                "validate");
    }

    @Test
    @Disabled
    // FIXME Markdown
    void RULE_COMPANY_NOT_BLABLA() {
        node = parse(RULE_COMPANY_NOT_BLABLA.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(5);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(5);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "not",
                "account company = BLABLACAR",
                "validate");
    }

    static Node parse(Metadata metadata) {
        return Parser.builder().build().parse(metadata.markdown(LOCALE));
    }

    static String render(Node doc) {
        return TextContentRenderer.builder().build().render(doc);
    }

    @AfterEach
    void afterEach() {
        System.out.println(render(node));
    }
}
