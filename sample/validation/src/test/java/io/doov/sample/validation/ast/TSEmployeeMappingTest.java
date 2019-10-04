/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.ast;

import static io.doov.assertions.ts.Assertions.assertParenthesis;
import static io.doov.assertions.ts.Assertions.assertThat;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.sample.validation.EmployeeMapping.*;
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
import io.doov.core.dsl.lang.MappingRule;
import io.doov.ts.ast.AstTSRenderer;
import io.doov.ts.ast.writer.DefaultTypeScriptWriter;
import io.doov.ts.ast.writer.TypeScriptWriter;
import io.doov.tsparser.TypeScriptParser;

class TSEmployeeMappingTest {

    private MappingRule rule;
    private String ruleTs;

    @AfterEach
    void tearDown() {
        System.out.println(ruleTs);
    }

    @Test
    void fullname_mapping() throws IOException {
        rule = FULLNAME_MAPPING;
        ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "and", "using", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "FIRST_NAME");
        assertThat(script).identifierExpressionsText().containsExactly("LAST_NAME", "combineNames", "FULLNAME");
        assertThat(script).literalsText().isEmpty();
    }

    @Test
    void email_mapping() throws IOException {
        rule = EMAIL_MAPPING;
        ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "is", "then", "map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "EMAIL_ACCEPTED", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("EMAIL", "EMAIL");
        assertThat(script).literalsText().containsExactly("true");
    }

    @Test
    void age_mapping() throws IOException {
        rule = AGE_MAPPING;
        ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "ageAt", "newDate", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "BIRTHDATE", "DateUtils");
        assertThat(script).identifierExpressionsText().containsExactly("AGE");
        assertThat(script).literalsText().containsExactly("'2019-01-01'");
    }

    @Test
    void country_mapping() throws IOException {
        rule = COUNTRY_MAPPING;
        ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "using", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("COUNTRY", "countryName", "COUNTRY");
        assertThat(script).literalsText().isEmpty();
    }

    @Test
    void company_mapping() throws IOException {
        rule = COMPANY_MAPPING;
        ruleTs = toTS(rule);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(script).errors().hasSize(0);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "using", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("COMPANY", "companyName", "COMPANY");
        assertThat(script).literalsText().isEmpty();
    }

    @Test
    void all_mappings() throws IOException {
        rule = ALL_MAPPINGS;
        ruleTs = toTS(ALL_MAPPINGS);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext context = parseAs(ruleTs, TypeScriptParser::script);
        assertThat(context).errors().hasSize(0);
        assertThat(context).numberOfSyntaxErrors().isEqualTo(0);
    }

    private String toTS(DSLBuilder dslBuilder) {
        final ByteArrayOutputStream ops = new ByteArrayOutputStream();
        TypeScriptWriter writer = new DefaultTypeScriptWriter(Locale.US, ops, BUNDLE);
        new AstTSRenderer(writer, true).toTS(dslBuilder.metadata());
        return new String(ops.toByteArray(), UTF_8);
    }

    private TypeScriptAssertionContext parseAs(String ruleTs, Function<TypeScriptParser, ParseTree> contextGetter)
            throws IOException {
        TypeScriptAssertionContext context = parseUsing(ruleTs, TypeScriptAssertionContext::new);
        new ParseTreeWalker().walk(context, contextGetter.apply(context.getParser()));
        return context;
    }

}
