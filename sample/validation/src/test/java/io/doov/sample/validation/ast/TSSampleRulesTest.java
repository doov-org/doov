/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.ast;

import static io.doov.assertions.ts.Assertions.assertParenthesis;
import static io.doov.assertions.ts.Assertions.assertThat;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.sample.validation.SampleRules.*;
import static io.doov.tsparser.util.TypeScriptParserFactory.parseUsing;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.function.Function;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.*;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.lang.DSLBuilder;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.ts.ast.AstTSRenderer;
import io.doov.ts.ast.writer.DefaultTypeScriptWriter;
import io.doov.ts.ast.writer.TypeScriptWriter;
import io.doov.tsparser.TypeScriptParser;

class TSSampleRulesTest {

    private ValidationRule rule;
    private String ruleTs;

    @AfterEach
    void tearDown() {
        System.out.println(ruleTs);
    }

    @Test
    void rule_email() throws IOException {
        rule = RULE_EMAIL;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "matches", "or", "matches", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "EMAIL", "EMAIL");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().containsExactly("'\\w+[@]\\w+\\.com'", "'\\w+[@]\\w+\\.fr'");
    }

    @Test
    void rule_account() throws IOException {
        rule = RULE_ACCOUNT;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText()
                .containsExactly("when", "matchAll", "ageAt", "greaterOrEquals", "length", "lesserOrEquals", "eq",
                        "and", "startsWith", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV", "BIRTHDATE", "EMAIL", "COUNTRY"
                , "PHONE_NUMBER");
        assertThat(script).identifierExpressionsText().containsExactly("today", "CONFIGURATION_EMAIL_MAX_SIZE", "FR");
        assertThat(script).literalsText().containsExactly("18", "'+33'");
    }

    @Test
    void rule_account2() throws IOException {
        rule = RULE_ACCOUNT_2;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText()
                .containsExactly("when",
                        "ageAt",
                        "greaterOrEquals",
                        "and",
                        "length",
                        "lesserOrEquals",
                        "and",
                        "eq",
                        "and",
                        "startsWith",
                        "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "BIRTHDATE", "EMAIL", "COUNTRY",
                "PHONE_NUMBER");
        assertThat(script).identifierExpressionsText().containsExactly("today", "CONFIGURATION_EMAIL_MAX_SIZE", "FR");
        assertThat(script).literalsText().containsExactly("18", "'+33'");
    }

    @Test
    void rule_user() throws IOException {
        rule = RULE_USER;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when",
                "count",
                "isNotNull",
                "isNotNull",
                "and",
                "matches",
                "greaterOrEquals",
                "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV", "FIRST_NAME", "LAST_NAME",
                "LAST_NAME");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().containsExactly("'[A-Z]+'", "0");
    }

    @Test
    void rule_user2() throws IOException {
        rule = RULE_USER_2;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when",
                "isNotNull",
                "and",
                "matches",
                "and",
                "count",
                "isNotNull",
                "isNotNull",
                "greaterThan",
                "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "LAST_NAME", "LAST_NAME", "DOOV",
                "PHONE_NUMBER", "EMAIL");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().containsExactly("'[A-Z]+'", "0");
    }

    @Test
    void rule_user_adult() throws IOException {
        rule = RULE_USER_ADULT;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "ageAt", "greaterOrEquals", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "BIRTHDATE");
        assertThat(script).identifierExpressionsText().containsExactly("CREATION_DATE");
        assertThat(script).literalsText().containsExactly("18");
    }

    @Test
    @Disabled
        // FIXME
    void rule_user_adult_firstday() throws IOException {
        rule = RULE_USER_ADULT_FIRSTDAY;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).rules(TypeScriptParser.IdentifierNameContext.class).isNotEmpty();
    }

    @Test
    @Disabled
        // FIXME
    void rule_first_name() throws IOException {
        rule = RULE_FIRST_NAME;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).rules(TypeScriptParser.IdentifierNameContext.class).isNotEmpty();
    }

    @Test
    void rule_id() throws IOException {
        rule = RULE_ID;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "isNotNull", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "USER_ID");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().isEmpty();
    }

    @Test
    void rule_age() throws IOException {
        rule = RULE_AGE;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "ageAt", "greaterOrEquals", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "BIRTHDATE");
        assertThat(script).identifierExpressionsText().containsExactly("today");
        assertThat(script).literalsText().containsExactly("18");
    }

    @Test
    @Disabled
        // FIXME
    void rule_age2() throws IOException {
        rule = RULE_AGE_2;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).rules(TypeScriptParser.IdentifierNameContext.class).isNotEmpty();
    }

    @Test
    void rule_sum() throws IOException {
        rule = RULE_SUM;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "sum", "times", "times", "greaterOrEquals",
                "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV", "CONFIGURATION_MIN_AGE",
                "CONFIGURATION_EMAIL_MAX_SIZE");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().containsExactly("0", "1", "0");
    }

    @Test
    void rule_min() throws IOException {
        rule = RULE_MIN;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "min", "greaterOrEquals", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("CONFIGURATION_MIN_AGE",
                "CONFIGURATION_EMAIL_MAX_SIZE");
        assertThat(script).literalsText().containsExactly("0");
    }

    @Test
    void rule_double_lambda() throws IOException {
        rule = RULE_DOUBLE_LAMBDA;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "anyMatchValues", "noOperator", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "FAVORITE_SITE_NAME_1", "DOOV");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().isEmpty();
    }

    @Test
    @Disabled
        // FIXME
    void rule_born_1980() throws IOException {
        rule = RULE_BORN_1980;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).rules(TypeScriptParser.IdentifierNameContext.class).isNotEmpty();
    }

    @Test
    @Disabled
        // FIXME
    void rule_account_time_contains() throws IOException {
        rule = RULE_ACCOUNT_TIME_CONTAINS;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).rules(TypeScriptParser.IdentifierNameContext.class).isNotEmpty();
    }

    @Test
    void rule_company_not() throws IOException {
        rule = RULE_COMPANY_NOT_BLABLA;
        this.ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "eq", "not", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "COMPANY");
        assertThat(script).identifierExpressionsText().containsExactly("BLABLACAR");
        assertThat(script).literalsText().isEmpty();
    }

    private String toTS(DSLBuilder dslBuilder) {
        final ByteArrayOutputStream ops = new ByteArrayOutputStream();
        TypeScriptWriter writer = new DefaultTypeScriptWriter(Locale.US, ops, BUNDLE);
        new AstTSRenderer(writer).toTS(dslBuilder.metadata());
        return new String(ops.toByteArray(), UTF_8);
    }

    private TypeScriptAssertionContext parseAs(String ruleTs, Function<TypeScriptParser, ParseTree> contextGetter)
            throws IOException {
        TypeScriptAssertionContext context = parseUsing(ruleTs, TypeScriptAssertionContext::new);
        new ParseTreeWalker().walk(context, contextGetter.apply(context.getParser()));
        return context;
    }

}
