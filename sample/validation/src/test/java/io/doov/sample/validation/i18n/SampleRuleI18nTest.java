/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.i18n;

import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.sample.validation.SampleRules.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.meta.SyntaxTree;
import io.doov.core.dsl.meta.ast.AstLineVisitor;

public class SampleRuleI18nTest {
    private static String astToString(SyntaxTree syntaxTree, Locale locale) {
        final StringBuilder sb = new StringBuilder();
        syntaxTree.accept(new AstLineVisitor(sb, BUNDLE, locale));
        return sb.toString();
    }
    
    @Test
    public void test_RULE_EMAIL() {
        System.out.println(astToString(RULE_EMAIL, Locale.FRANCE));
    }

    @Test
    public void test_RULE_ACCOUNT() {
        System.out.println(astToString(RULE_ACCOUNT, Locale.FRANCE));
    }

    @Test
    public void test_RULE_ACCOUNT_2() {
        System.out.println(astToString(RULE_ACCOUNT_2, Locale.FRANCE));
    }

    @Test
    public void test_RULE_USER() {
        System.out.println(astToString(RULE_USER, Locale.FRANCE));
    }

    @Test
    public void test_RULE_USER_2() {
        System.out.println(astToString(RULE_USER_2, Locale.FRANCE));
    }

    @Test
    public void test_RULE_USER_ADULT() {
        System.out.println(astToString(RULE_USER_ADULT, Locale.FRANCE));
    }

    @Test
    public void test_RULE_USER_ADULT_FIRSTDAY() {
        System.out.println(astToString(RULE_USER_ADULT_FIRSTDAY, Locale.FRANCE));
    }

    @Test
    public void test_RULE_FIRST_NAME() {
        System.out.println(astToString(RULE_FIRST_NAME, Locale.FRANCE));
    }

    @Test
    public void test_RULE_ID() {
        System.out.println(astToString(RULE_ID, Locale.FRANCE));
    }

    @Test
    public void test_RULE_AGE_2() {
        System.out.println(astToString(RULE_AGE_2, Locale.FRANCE));
    }

    @Test
    public void test_RULE_SUM() {
        System.out.println(astToString(RULE_SUM, Locale.FRANCE));
    }

    @Test
    public void test_RULE_MIN() {
        System.out.println(astToString(RULE_MIN, Locale.FRANCE));
    }
}
