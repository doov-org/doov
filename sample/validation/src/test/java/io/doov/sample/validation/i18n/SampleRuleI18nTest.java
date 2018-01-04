/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.i18n;

import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.sample.field.SampleFieldIdInfo.accountEmail;
import static io.doov.sample.validation.SampleRules.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.SyntaxTree;
import io.doov.core.dsl.meta.ast.AstLineVisitor;

public class SampleRuleI18nTest {
    private static String toString(SyntaxTree syntaxTree, Locale locale) {
        final StringBuilder sb = new StringBuilder();
        syntaxTree.accept(new AstLineVisitor(sb, BUNDLE, locale));
        return sb.toString();
    }

    @Test
    public void test_RULE_EMAIL() {
        System.out.println(toString(RULE_EMAIL, Locale.FRANCE));
    }

    @Test
    public void test_RULE_ACCOUNT() {
        System.out.println(toString(RULE_ACCOUNT, Locale.FRANCE));
    }

    @Test
    public void test_RULE_ACCOUNT_2() {
        System.out.println(toString(RULE_ACCOUNT_2, Locale.FRANCE));
    }

    @Test
    public void test_RULE_USER() {
        System.out.println(toString(RULE_USER, Locale.FRANCE));
    }

    @Test
    public void test_RULE_USER_2() {
        System.out.println(toString(RULE_USER_2, Locale.FRANCE));
    }

    @Test
    public void test_RULE_USER_ADULT() {
        System.out.println(toString(RULE_USER_ADULT, Locale.FRANCE));
    }

    @Test
    public void test_RULE_USER_ADULT_FIRSTDAY() { System.out.println(toString(RULE_USER_ADULT_FIRSTDAY, Locale.FRANCE)); }

    @Test
    public void test_RULE_FIRST_NAME() {
        System.out.println(toString(RULE_FIRST_NAME, Locale.FRANCE));
    }

    @Test
    public void test_RULE_ID() {
        System.out.println(toString(RULE_ID, Locale.FRANCE));
    }

    @Test
    public void test_RULE_AGE_2() {
        System.out.println(toString(RULE_AGE_2, Locale.FRANCE));
    }

    @Test
    public void test_RULE_SUM() { System.out.println(toString(RULE_SUM, Locale.FRANCE)); }

    @Test
    public void test_RULE_MIN() {
        System.out.println(toString(RULE_MIN, Locale.FRANCE));
    }

    @Test
    public void test_RULE_DOUBLE_TEMPORAL() { System.out.println(toString(RULE_DOUBLE_TEMPORAL, Locale.FRANCE)); }

    @Test
    public void test_match_any() {
        ValidationRule rule = DOOV.when(matchAny(accountEmail().startsWith("a"),
                        accountEmail().startsWith("b"))).validate();
        System.out.println(toString(rule, Locale.FRANCE));
    }

    @Test
    public void test_count_greater_than_2() {
        ValidationRule rule = DOOV.when(DOOV.count(accountEmail().startsWith("a"),
                        accountEmail().startsWith("b")).greaterThan(2)).validate();
        System.out.println(toString(rule, Locale.FRANCE));
    }
}
