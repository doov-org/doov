/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast;

import static io.doov.assertions.ts.Assertions.assertThat;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.tsparser.util.TypeScriptParserFactory.parseUsing;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.ts.ast.writer.DefaultTypeScriptWriter;
import io.doov.ts.ast.writer.TypeScriptWriter;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptIterableTest {

    private MappingRule rule;
    private String ruleTs;

    @Test
    void test_iterable() throws IOException {

        List<String> content = Arrays.asList("1", "2", "3");

        GenericModel model = new GenericModel();
        IterableFieldInfo<String, List<String>> items = model.iterableField(content, "items");

        rule = DOOV.mapIter(content).to(items);

        ruleTs = toTS(rule.executeOn(model, model).getRootMetadata());
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("items");
        assertThat(script).literalsText().containsExactly("'1'", "'2'", "'3'");
        assertThat(script).arrayLiteralsText().containsExactly("['1','2','3']");
    }

    @Test
    void test_iterable_array() throws IOException {

        GenericModel model = new GenericModel();
        IterableFieldInfo<String, List<String>> items = model.iterableField(null, "items");

        rule = DOOV.mapIter("1", "2", "3").to(items);

        ruleTs = toTS(rule.executeOn(model, model).getRootMetadata());
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("items");
        assertThat(script).literalsText().containsExactly("'1'", "'2'", "'3'");
        assertThat(script).arrayLiteralsText().containsExactly("['1','2','3']");
    }

    @Test
    void test_iterable_int_array() throws IOException {

        GenericModel model = new GenericModel();
        IterableFieldInfo<Integer, List<Integer>> items = model.iterableField(null, "integerItems");

        rule = DOOV.mapIter(1, 2, 3).to(items);

        ruleTs = toTS(rule.executeOn(model, model).getRootMetadata());
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("integerItems");
        assertThat(script).literalsText().containsExactly("1", "2", "3");
        assertThat(script).arrayLiteralsText().containsExactly("[1,2,3]");
    }

    private static String toTS(Metadata metadata) {
        final ByteArrayOutputStream ops = new ByteArrayOutputStream();
        TypeScriptWriter writer = new DefaultTypeScriptWriter(Locale.US, ops, BUNDLE,
                field -> field.id().code().replace(" ", ""));
        new AstTSRenderer(writer, true).toTS(metadata);
        return new String(ops.toByteArray(), UTF_8);
    }

    private static TypeScriptAssertionContext parseAs(String ruleTs,
            Function<TypeScriptParser, ParseTree> contextGetter)
            throws IOException {
        TypeScriptAssertionContext context = parseUsing(ruleTs, TypeScriptAssertionContext::new);
        new ParseTreeWalker().walk(context, contextGetter.apply(context.getParser()));
        return context;
    }

    @AfterEach
    void tearDown() {
        System.out.println(ruleTs);
    }
}
