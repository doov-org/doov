/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.i18n;

import static io.doov.core.dsl.DOOV.count;
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE_MATCH_ANY;
import static io.doov.core.dsl.meta.MetadataType.LEAF_VALUE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.UNARY_PREDICATE;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfMonth;
import static io.doov.sample.field.dsl.DslSampleModel.accountCreationDate;
import static io.doov.sample.field.dsl.DslSampleModel.accountEmail;
import static io.doov.sample.field.dsl.DslSampleModel.configurationMaxEmailSize;
import static io.doov.sample.field.dsl.DslSampleModel.configurationMinAge;
import static io.doov.sample.field.dsl.DslSampleModel.userBirthdate;
import static io.doov.sample.validation.SampleRules.RULE_ACCOUNT;
import static io.doov.sample.validation.SampleRules.RULE_ACCOUNT_2;
import static io.doov.sample.validation.SampleRules.RULE_ACCOUNT_TIME_CONTAINS;
import static io.doov.sample.validation.SampleRules.RULE_AGE;
import static io.doov.sample.validation.SampleRules.RULE_AGE_2;
import static io.doov.sample.validation.SampleRules.RULE_BORN_1980;
import static io.doov.sample.validation.SampleRules.RULE_DOUBLE_LAMBDA;
import static io.doov.sample.validation.SampleRules.RULE_EMAIL;
import static io.doov.sample.validation.SampleRules.RULE_FIRST_NAME;
import static io.doov.sample.validation.SampleRules.RULE_ID;
import static io.doov.sample.validation.SampleRules.RULE_MIN;
import static io.doov.sample.validation.SampleRules.RULE_SUM;
import static io.doov.sample.validation.SampleRules.RULE_USER;
import static io.doov.sample.validation.SampleRules.RULE_USER_2;
import static io.doov.sample.validation.SampleRules.RULE_USER_ADULT;
import static io.doov.sample.validation.SampleRules.RULE_USER_ADULT_FIRSTDAY;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.function.MapFunctionMetadata;
import io.doov.core.dsl.meta.function.NumericFunctionMetadata;
import io.doov.core.dsl.meta.function.StringFunctionMetadata;
import io.doov.core.dsl.meta.function.TemporalBiFunctionMetadata;
import io.doov.core.dsl.meta.function.TemporalFunctionMetadata;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.NaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.UnaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.ValuePredicateMetadata;

public class SampleRuleI18nTest {
    Metadata root;

    private static Metadata predicate(ValidationRule rule) {
        return rule.metadata().children().findFirst().get().children().findFirst().get();
    }

    @AfterEach
    void printPredicate() {
        System.out.println(root);
    }

    @Test
    void test_RULE_EMAIL() {
        root = predicate(RULE_EMAIL);
        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(1)).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_RULE_ACCOUNT() {
        root = predicate(RULE_ACCOUNT);
        assertThat(root).isInstanceOf(NaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(NARY_PREDICATE);

        assertThat(root.childAt(0)).isInstanceOf(NumericFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);

        assertThat(root.childAt(1)).isInstanceOf(NumericFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(1, 0)).isInstanceOf(UnaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(UNARY_PREDICATE);
        assertThat(root.childAt(1, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);

        assertThat(root.childAt(2)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(2, 0)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(2, 1)).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
    }

    @Test
    void test_RULE_ACCOUNT_2() {
        root = predicate(RULE_ACCOUNT_2);
        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0, 0)).isInstanceOf(NumericFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0, 0, 0)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0, 0, 0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 0, 0, 0, 1)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(0, 0, 0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(0, 0, 1)).isInstanceOf(NumericFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0, 1, 0)).isInstanceOf(UnaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(UNARY_PREDICATE);
        assertThat(root.childAt(0, 0, 1, 0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(1)).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_RULE_USER() {
        root = predicate(RULE_USER);
        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(NaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(NARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(UnaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(UNARY_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 1, 0)).isInstanceOf(UnaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(UNARY_PREDICATE);
        assertThat(root.childAt(0, 1, 0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1, 1)).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 1, 1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1, 1, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_RULE_USER_2() {
        root = predicate(RULE_USER_2);
        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(UnaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(UNARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);

        assertThat(root.childAt(1)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(1, 0)).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(1, 0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1, 0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);

        assertThat(root.childAt(1, 1)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(1, 1, 0)).isInstanceOf(NaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(NARY_PREDICATE);
        assertThat(root.childAt(1, 1, 0, 0)).isInstanceOf(UnaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(UNARY_PREDICATE);
        assertThat(root.childAt(1, 1, 0, 0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1, 1, 0, 1)).isInstanceOf(UnaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(UNARY_PREDICATE);
        assertThat(root.childAt(1, 1, 0, 1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);

        assertThat(root.childAt(1, 1, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_RULE_USER_ADULT() {
        root = predicate(RULE_USER_ADULT);
        assertThat(root).isInstanceOf(NumericFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_RULE_USER_ADULT_FIRSTDAY() {
        root = predicate(RULE_USER_ADULT_FIRSTDAY);
        assertThat(root).isInstanceOf(NumericFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1, 1)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_RULE_FIRST_NAME() {
        root = predicate(RULE_FIRST_NAME);
        assertThat(root).isInstanceOf(NaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(NARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(MapFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_RULE_ID() {
        root = predicate(RULE_ID);
        assertThat(root).isInstanceOf(UnaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(UNARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);

    }

    @Test
    void test_RULE_AGE_2() {
        root = predicate(RULE_AGE_2);
        assertThat(root).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1, 1)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_RULE_SUM() {
        root = predicate(RULE_SUM);
        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(NaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(NARY_PREDICATE);

        assertThat(root.childAt(0, 0)).isInstanceOf(NumericFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);

        assertThat(root.childAt(0, 1)).isInstanceOf(NumericFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);

        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_RULE_MIN() {
        root = predicate(RULE_MIN);
        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(NaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(NARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_RULE_DOUBLE_TEMPORAL() {
        root = predicate(when(userBirthdate.with(firstDayOfMonth())
                .ageAt(accountCreationDate.with(firstDayOfMonth())).lesserThan(18)).validate());

        assertThat(root).isInstanceOf(NumericFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 0, 1)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(0, 1)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1, 1)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_match_any() {
        root = predicate(when(matchAny(accountEmail.startsWith("a"),
                accountEmail.startsWith("b"))).validate());

        assertThat(root).isInstanceOf(NaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(NARY_PREDICATE);

        assertThat(root.childAt(0)).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);

        assertThat(root.childAt(1)).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_count_greater_than_2() {
        root = predicate(when(count(accountEmail.startsWith("a"),
                accountEmail.startsWith("b")).greaterThan(2)).validate());

        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(NaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(NARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(0, 1)).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(1)).isInstanceOf(LeafMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_combined_when() {
        root = predicate(
                when(configurationMinAge.when(configurationMaxEmailSize.anyMatch(11, 12, 13)).eq(1)).validate());

        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 1, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE_MATCH_ANY);
        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_temporal_unit() {
        root = predicate(when(userBirthdate.minus(1, YEARS).before(today())).validate());

        assertThat(root).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(1)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_eq_today() {
        root = predicate(when(userBirthdate.eq(today())).validate());
        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_eq_today_plus_1_day() {
        root = predicate(when(userBirthdate.eq(today().plus(1, DAYS))).validate());

        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(1, 0)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(1, 1)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_age_at_today() {
        root = predicate(RULE_AGE);
        assertThat(root).isInstanceOf(NumericFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(TemporalBiFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(TemporalFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);

    }

    @Test
    void test_double_lambda() {
        root = predicate(RULE_DOUBLE_LAMBDA);
        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE_MATCH_ANY);
    }

    @Test
    void test_map_to_int() {
        root = predicate(RULE_BORN_1980);
        assertThat(root).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(BinaryPredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }

    @Test
    void test_map_to_string() {
        root = predicate(RULE_ACCOUNT_TIME_CONTAINS);
        assertThat(root).isInstanceOf(StringFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0)).isInstanceOf(MapFunctionMetadata.class)
                .extracting(Metadata::type).containsExactly(BINARY_PREDICATE);
        assertThat(root.childAt(0, 0)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(FIELD_PREDICATE);
        assertThat(root.childAt(0, 1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
        assertThat(root.childAt(1)).isInstanceOf(ValuePredicateMetadata.class)
                .extracting(Metadata::type).containsExactly(LEAF_VALUE);
    }
}
