/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.ast;

import static io.doov.core.dsl.meta.ast.AstHtmlRenderer.astToHtml;
import static io.doov.sample.validation.SampleRules.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jsoup.Jsoup.parseBodyFragment;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.*;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;
import io.doov.sample.model.SampleModels;

public class HtmlSampleRulesTest {

    private static final Locale LOCALE = Locale.US;

    private final DslModel sample = SampleModels.wrapper();
    private Result result;
    private Document doc;

    @Test
    @Disabled
    // FIXME broken since leaf metadata refactoring
    void RULE_EMAIL() {
        result = resetCounters(RULE_EMAIL).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "0 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("matches", "matches");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("account.email", "account.email");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("'\\w+[@]\\w+\\.com'", "'\\w+[@]\\w+\\.fr'");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("or");
    }

    @Test
    @Disabled
    // FIXME broken since leaf metadata refactoring
    void RULE_ACCOUNT() {
        result = resetCounters(RULE_ACCOUNT).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);
        assertTrue(result.value());

        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(2);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "100 %", "100 %", "100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("age at", "today", ">=", "length is", "<=", "=", "starts with");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("18", "FR", "'+33'");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.birthdate", "account.email", "configuration.max.email.size",
                        "account.country", "account.phone.number");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("match all");
    }

    @Test
    @Disabled
    // FIXME broken since leaf metadata refactoring
    void RULE_ACCOUNT_2() {
        result = resetCounters(RULE_ACCOUNT_2).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "100 %", "100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("age at", "today", ">=", "length is", "<=", "=", "starts with");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("18", "FR", "'+33'");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.birthdate", "account.email", "configuration.max.email.size",
                        "account.country", "account.phone.number");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("and", "and", "and");
    }

    @Test
    @Disabled
    // FIXME broken since leaf metadata refactoring
    void RULE_USER() {
        result = resetCounters(RULE_USER).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "100 %", "100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("is not null", "is not null", "matches");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("'[A-Z]+'", "0");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.first.name", "user.last.name", "user.last.name");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("count");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("and", ">=");
    }

    @Test
    @Disabled
    // FIXME broken since leaf metadata refactoring
    void RULE_USER_2() {
        result = resetCounters(RULE_USER_2).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(2);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(2);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "100 %", "100 %", "100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("is not null", "matches", "is not null", "is not null");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("'[A-Z]+'", "0");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.last.name", "user.last.name", "account.phone.number", "account.email");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("count");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("and", "and", ">");
        assertThat(doc.select("ul.dsl-ul-binary")).extracting(Element::text)
                .containsExactly("100 % user.last.name matches '[A-Z]+' and 100 % count 100 % account.phone.number is" +
                        " not null 100 % account.email is not null > 0");
    }

    @Test
    void RULE_USER_ADULT() {
        result = resetCounters(RULE_USER_ADULT).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);
        assertTrue(result.value());

        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly();
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("age at", ">=");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("18");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.birthdate", "account.creation.date");
    }

    @Test
    @Disabled
    // FIXME broken since leaf metadata refactoring
    void RULE_USER_ADULT_FIRSTDAY() {
        result = resetCounters(RULE_USER_ADULT_FIRSTDAY).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("age at", "with", "first day of year", ">=");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("18");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.birthdate", "account.creation.date");
    }

    @Test
    void RULE_FIRST_NAME() {
        result = resetCounters(RULE_FIRST_NAME).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(2);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly();
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("as a number", "=");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("1");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.first.name");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("match all");
        assertThat(doc.select("span.dsl-token-unknown")).extracting(Element::text)
                .containsExactly("-function-");
    }

    @Test
    void RULE_ID() {
        result = resetCounters(RULE_ID).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-unary")).extracting(Element::text)
                .containsExactly("is not null");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.id");
    }

    @Test
    void RULE_AGE() {
        result = resetCounters(RULE_AGE).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("today");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("age at", ">=");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.birthdate");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("18");
    }

    @Test
    @Disabled
    // FIXME broken since leaf metadata refactoring
    void RULE_AGE_2() {
        result = resetCounters(RULE_AGE_2).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("after", "minus", "day(s)");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.birthdate", "user.birthdate");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("1");
    }

    @Test
    void RULE_SUM() {
        result = resetCounters(RULE_SUM).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(2);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("configuration.min.age", "configuration.max.email.size");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("sum");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly();
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("x", "x", ">=");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("0", "1", "0");
    }

    @Test
    void RULE_MIN() {
        result = resetCounters(RULE_MIN).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(2);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("configuration.min.age", "configuration.max.email.size");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("min");
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly(">=");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("0");
    }

    @Test
    void RULE_DOUBLE_LAMBDA() {
        result = resetCounters(RULE_DOUBLE_LAMBDA).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly();
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("match any");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("favorite.site.name.1");
        assertThat(doc.select("span.dsl-token-unknown")).extracting(Element::text)
                .containsExactly();
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("-function-");
    }

    @Test
    void RULE_BORN_1980() {
        result = resetCounters(RULE_BORN_1980).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly();
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("as a number", "=");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("user.birthdate");
        assertThat(doc.select("span.dsl-token-unknown")).extracting(Element::text)
                .containsExactly("-function-");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("1980");
    }

    @Test
    void RULE_ACCOUNT_TIME_CONTAINS() {
        result = resetCounters(RULE_ACCOUNT_TIME_CONTAINS).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly();
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("as a string", "contains");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("account.timezone");
        assertThat(doc.select("span.dsl-token-unknown")).extracting(Element::text)
                .containsExactly("-function-");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("'00:00'");
    }

    @Test
    void RULE_COMPANY_NOT_LESFURETS() {
        result = resetCounters(RULE_COMPANY_NOT_LESFURETS).withShortCircuit(false).executeOn(sample);
        doc = documentOf(result);

        assertTrue(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("li.dsl-li-unary")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(1);

        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly();
        assertThat(doc.select("span.dsl-token-binary")).extracting(Element::text)
                .containsExactly("=");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("BLABLACAR");
        assertThat(doc.select("span.dsl-token-field")).extracting(Element::text)
                .containsExactly("account.company");
        assertThat(doc.select("span.dsl-token-unary")).extracting(Element::text)
                .containsExactly("not");
        assertThat(doc.select("ul.dsl-ul-unary")).extracting(Element::text)
                .containsExactly("account.company = BLABLACAR 100 %");
    }

    static Document documentOf(Result result) {
        return parseBodyFragment(astToHtml(result.getContext().getRootMetadata(), LOCALE));
    }

    static String format(Result result, Document doc) {
        return "<!-- " + AstVisitorUtils.astToString(result.getContext().getRootMetadata(), LOCALE) + " -->\n"
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
