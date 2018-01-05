/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.i18n;

import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfMonth;
import static io.doov.sample.field.SampleFieldIdInfo.accountCreationDate;
import static io.doov.sample.field.SampleFieldIdInfo.accountEmail;
import static io.doov.sample.field.SampleFieldIdInfo.userBirthdate;
import static io.doov.sample.validation.SampleRules.RULE_ACCOUNT;
import static io.doov.sample.validation.SampleRules.RULE_ACCOUNT_2;
import static io.doov.sample.validation.SampleRules.RULE_AGE_2;
import static io.doov.sample.validation.SampleRules.RULE_EMAIL;
import static io.doov.sample.validation.SampleRules.RULE_FIRST_NAME;
import static io.doov.sample.validation.SampleRules.RULE_ID;
import static io.doov.sample.validation.SampleRules.RULE_MIN;
import static io.doov.sample.validation.SampleRules.RULE_SUM;
import static io.doov.sample.validation.SampleRules.RULE_USER;
import static io.doov.sample.validation.SampleRules.RULE_USER_2;
import static io.doov.sample.validation.SampleRules.RULE_USER_ADULT;
import static io.doov.sample.validation.SampleRules.RULE_USER_ADULT_FIRSTDAY;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.NaryMetadata;
import io.doov.core.dsl.meta.SyntaxTree;
import io.doov.core.dsl.meta.ast.AstLineVisitor;

public class SampleRuleI18nTest {
    private static void print(SyntaxTree tree) {
        final StringBuilder sb = new StringBuilder();
        tree.accept(new AstLineVisitor(sb, BUNDLE, Locale.FRANCE), 0);
        System.out.println(sb.toString());
    }

    @Test
    public void test_RULE_EMAIL() {
        final ValidationRule rule = RULE_EMAIL;
        assertThat(rule.getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(FIELD_PREDICATE);

        print(rule);
    }

    @Test
    public void test_RULE_ACCOUNT() {
        final ValidationRule rule = RULE_ACCOUNT;
        assertThat(rule.getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(NARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(2)).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(2).type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(2).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(2).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(2).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(2).children().get(1).type()).isEqualTo(FIELD_PREDICATE);

        print(rule);
    }

    @Test
    public void test_RULE_ACCOUNT_2() {
        final ValidationRule rule = RULE_ACCOUNT_2;

        assertThat(rule.getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        print(rule);
    }

    @Test
    public void test_RULE_USER() {
        final ValidationRule rule = RULE_USER;
        print(rule);
    }

    @Test
    public void test_RULE_USER_2() {
        final ValidationRule rule = RULE_USER_2;
        print(rule);
    }

    @Test
    public void test_RULE_USER_ADULT() {
        final ValidationRule rule = RULE_USER_ADULT;
        print(rule);
    }

    @Test
    public void test_RULE_USER_ADULT_FIRSTDAY() {
        final ValidationRule rule = RULE_USER_ADULT_FIRSTDAY;
        print(rule);
    }

    @Test
    public void test_RULE_FIRST_NAME() {
        final ValidationRule rule = RULE_FIRST_NAME;
        print(rule);
    }

    @Test
    public void test_RULE_ID() {
        final ValidationRule rule = RULE_ID;
        print(rule);
    }

    @Test
    public void test_RULE_AGE_2() {
        final ValidationRule rule = RULE_AGE_2;
        print(rule);
    }

    @Test
    public void test_RULE_SUM() {
        final ValidationRule rule = RULE_SUM;
        print(rule);
    }

    @Test
    public void test_RULE_MIN() {
        final ValidationRule rule = RULE_MIN;
        print(rule);
    }

    @Test
    public void test_RULE_DOUBLE_TEMPORAL() {
        ValidationRule rule = DOOV
                        .when(userBirthdate().with(firstDayOfMonth())
                                        .ageAt(accountCreationDate().with(firstDayOfMonth()))
                                        .lesserThan(18))
                        .validate();
        print(rule);
    }

    @Test
    public void test_match_any() {
        ValidationRule rule = DOOV.when(matchAny(accountEmail().startsWith("a"),
                        accountEmail().startsWith("b"))).validate();
        print(rule);
    }

    @Test
    public void test_count_greater_than_2() {
        ValidationRule rule = DOOV.when(DOOV.count(accountEmail().startsWith("a"),
                        accountEmail().startsWith("b")).greaterThan(2)).validate();
        print(rule);
    }
}
