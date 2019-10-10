/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.ast;

import static io.doov.assertions.renderer.Assertions.assertThat;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.toHtml;
import static io.doov.sample.validation.SampleRules.*;
import static org.jsoup.Jsoup.parseBodyFragment;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;
import io.doov.sample.model.SampleModels;

public class HtmlSampleRulesTest {

    private static final Locale LOCALE = Locale.US;

    private final FieldModel sample = SampleModels.wrapper();
    private Result result;
    private Document doc;

    @Test
    void RULE_EMAIL() {
        result = resetCounters(RULE_EMAIL).withShortCircuit(false).executeOn(sample);
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

        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("matches", "or", "matches");
        assertThat(doc).tokenField_SPAN().containsExactly("account email", "account email");
        assertThat(doc).tokenValue_SPAN().containsExactly("'\\w+[@]\\w+\\.com'", "'\\w+[@]\\w+\\.fr'");
    }

    @Test
    void RULE_ACCOUNT() {
        result = resetCounters(RULE_ACCOUNT).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);
        assertTrue(result.value());

        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(3);
        assertThat(doc).nary_LI().hasSize(1);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "100 %", "100 %", "100 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("age at", "today", ">=", "length is", "<=", "=", "and",
                "starts with");
        assertThat(doc).tokenValue_SPAN().containsExactly("18", "FR", "'+33'");
        assertThat(doc).tokenField_SPAN().containsExactly("user birthdate", "account email",
                "configuration max email size",
                "account country", "account phone number");
        assertThat(doc).tokenNary_SPAN().containsExactly("match all");
    }

    @Test
    void RULE_ACCOUNT_2() {
        result = resetCounters(RULE_ACCOUNT_2).withShortCircuit(false).executeOn(sample);
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

        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "100 %", "100 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("age at", "today", ">=", "and", "length is", "<=", "and",
                "=",
                "and", "starts with");
        assertThat(doc).tokenValue_SPAN().containsExactly("18", "FR", "'+33'");
        assertThat(doc).tokenField_SPAN().containsExactly("user birthdate", "account email",
                "configuration max email size",
                "account country", "account phone number");
    }

    @Test
    void RULE_USER() {
        result = resetCounters(RULE_USER).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(1);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(1);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "100 %", "100 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("is not null", "is not null", "and", "matches", ">=");
        assertThat(doc).tokenValue_SPAN()
                .containsExactly("'[A-Z]+'", "0");
        assertThat(doc).tokenField_SPAN()
                .containsExactly("user first name", "user last name", "user last name");
        assertThat(doc).tokenNary_SPAN()
                .containsExactly("count");
    }

    @Test
    void RULE_USER_2() {
        result = resetCounters(RULE_USER_2).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(1);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(2);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "100 %", "100 %", "100 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("is not null", "and", "matches", "and", "is not null",
                "is not null", ">");
        assertThat(doc).tokenValue_SPAN().containsExactly("'[A-Z]+'", "0");
        assertThat(doc).tokenField_SPAN().containsExactly("user last name", "user last name", "account phone number",
                "account email");
        assertThat(doc).tokenNary_SPAN().containsExactly("count");
        assertThat(doc).BODY().containsExactly("100 % user last name is not null and "
                + "100 % user last name matches '[A-Z]+' and "
                + "100 % count 100 % account phone number is not null "
                + "100 % account email is not null > 0");
    }

    @Test
    void RULE_USER_ADULT() {
        result = resetCounters(RULE_USER_ADULT).withShortCircuit(false).executeOn(sample);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("age at", ">=");
        assertThat(doc).tokenValue_SPAN().containsExactly("18");
        assertThat(doc).tokenField_SPAN().containsExactly("user birthdate", "account creation date");
    }

    @Test
    void RULE_USER_ADULT_FIRSTDAY() {
        result = resetCounters(RULE_USER_ADULT_FIRSTDAY).withShortCircuit(false).executeOn(sample);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("age at", "with", "first day of year", ">=");
        assertThat(doc).tokenValue_SPAN().containsExactly("18");
        assertThat(doc).tokenField_SPAN().containsExactly("user birthdate", "account creation date");
    }

    @Test
    void RULE_FIRST_NAME() {
        result = resetCounters(RULE_FIRST_NAME).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(1);
        assertThat(doc).nary_LI().hasSize(1);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("as a number", "=");
        assertThat(doc).tokenValue_SPAN().containsExactly("", "1");
        assertThat(doc).tokenField_SPAN().containsExactly("user first name");
        assertThat(doc).tokenNary_SPAN().containsExactly("match all");
        assertThat(doc).tokenUnknown_SPAN().isEmpty();
    }

    @Test
    void RULE_ID() {
        result = resetCounters(RULE_ID).withShortCircuit(false).executeOn(sample);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("is not null");
        assertThat(doc).tokenField_SPAN().containsExactly("user id");
    }

    @Test
    void RULE_AGE() {
        result = resetCounters(RULE_AGE).withShortCircuit(false).executeOn(sample);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("age at", "today", ">=");
        assertThat(doc).tokenField_SPAN().containsExactly("user birthdate");
        assertThat(doc).tokenValue_SPAN().containsExactly("18");
    }

    @Test
    void RULE_AGE_2() {
        result = resetCounters(RULE_AGE_2).withShortCircuit(false).executeOn(sample);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("after", "minus", "day(s)");
        assertThat(doc).tokenField_SPAN().containsExactly("user birthdate", "user birthdate");
        assertThat(doc).tokenValue_SPAN().containsExactly("1");
    }

    @Test
    void RULE_SUM() {
        result = resetCounters(RULE_SUM).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(2);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().containsExactly("100 %");
        assertThat(doc).tokenField_SPAN().containsExactly("configuration min age", "configuration max email size");
        assertThat(doc).tokenNary_SPAN().containsExactly("sum");
        assertThat(doc).tokenOperator_SPAN().containsExactly("x", "x", ">=");
        assertThat(doc).tokenValue_SPAN().containsExactly("0", "1", "0");
    }

    @Test
    void RULE_MIN() {
        result = resetCounters(RULE_MIN).withShortCircuit(false).executeOn(sample);
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
        assertThat(doc).tokenField_SPAN().containsExactly("configuration min age", "configuration max email size");
        assertThat(doc).tokenNary_SPAN().containsExactly("min");
        assertThat(doc).tokenOperator_SPAN().containsExactly(">=");
        assertThat(doc).tokenValue_SPAN().containsExactly("0");
    }

    @Test
    void RULE_DOUBLE_LAMBDA() {
        result = resetCounters(RULE_DOUBLE_LAMBDA).withShortCircuit(false).executeOn(sample);
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
        assertThat(doc).tokenField_SPAN().containsExactly("favorite site name 1");
        assertThat(doc).tokenUnknown_SPAN().isEmpty();
        assertThat(doc).tokenValue_SPAN().isEmpty();
    }

    @Test
    void RULE_BORN_1980() {
        result = resetCounters(RULE_BORN_1980).withShortCircuit(false).executeOn(sample);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("as a number", "=");
        assertThat(doc).tokenField_SPAN().containsExactly("user birthdate");
        assertThat(doc).tokenUnknown_SPAN().isEmpty();
        assertThat(doc).tokenValue_SPAN().containsExactly("", "1980");
    }

    @Test
    void RULE_ACCOUNT_TIME_CONTAINS() {
        result = resetCounters(RULE_ACCOUNT_TIME_CONTAINS).withShortCircuit(false).executeOn(sample);
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
        assertThat(doc).tokenOperator_SPAN().containsExactly("as a string", "contains");
        assertThat(doc).tokenField_SPAN().containsExactly("account timezone");
        assertThat(doc).tokenUnknown_SPAN().isEmpty();
        assertThat(doc).tokenValue_SPAN().containsExactly("", "'00:00'");
    }

    @Test
    void RULE_COMPANY_NOT_LESFURETS() {
        result = resetCounters(RULE_COMPANY_NOT_BLABLA).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).unary_LI().hasSize(0);
        assertThat(doc).unary_UL().hasSize(1);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().containsExactly("0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("not", "=");
        assertThat(doc).tokenValue_SPAN().containsExactly("BLABLACAR");
        assertThat(doc).tokenField_SPAN().containsExactly("account company");
        assertThat(doc).tokenUnary_SPAN().isEmpty();
        assertThat(doc).nary_UL().containsExactly("0 % account company = BLABLACAR");
    }

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

    static ValidationRule resetCounters(ValidationRule rule) {
        resetCounters(rule.getStepWhen().metadata());
        return rule;
    }

    static void resetCounters(Metadata metadata) {
        if (PredicateMetadata.class.isAssignableFrom(metadata.getClass()))
            ((PredicateMetadata) metadata).resetCounters();
        metadata.children().forEach(m -> resetCounters(m));
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }
}
