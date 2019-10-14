/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.ts.ast.test;

import static io.doov.assertions.ts.Assertions.assertParenthesis;
import static io.doov.assertions.ts.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.ts.ast.test.TypeScriptAnyMatchTest.EnumTest.VAL1;
import static io.doov.ts.ast.test.TypeScriptAnyMatchTest.EnumTest.VAL2;
import static io.doov.ts.ast.test.TypeScriptAnyMatchTest.EnumTest.VAL3;
import static io.doov.tsparser.util.TypeScriptParserFactory.parseUsing;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.function.Function;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.*;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.ts.ast.AstTSRenderer;
import io.doov.ts.ast.writer.DefaultTypeScriptWriter;
import io.doov.ts.ast.writer.TypeScriptWriter;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptAnyMatchTest {

    private StepCondition A;
    private Result result;
    private String ruleTs;

    private GenericModel model;
    private EnumFieldInfo<EnumTest> enumField;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.enumField = model.enumField(VAL1, "enumField");
    }

    @Test
    void anyMatch_success() throws IOException {
        result = when(enumField.anyMatch(VAL1, VAL2, VAL3)).validate().executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("matchAny", "VAL1", "VAL2", "VAL3");
        assertThat(script).identifierReferencesText().containsExactly("enumField");
        assertThat(script).identifierExpressionsText().containsExactly("EnumTest", "EnumTest", "EnumTest");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void anyMatch_failure() throws IOException {
        result = when(enumField.anyMatch(VAL2, VAL3)).validate().executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("matchAny", "VAL2", "VAL3");
        assertThat(script).identifierReferencesText().containsExactly("enumField");
        assertThat(script).identifierExpressionsText().containsExactly("EnumTest", "EnumTest");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void and_combined_anyMatch_success() throws IOException {
        A = DOOV.alwaysTrue("A");
        result = when(A.and(enumField.anyMatch(VAL1, VAL2, VAL3))).validate().executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("and", "matchAny", "VAL1", "VAL2", "VAL3");
        assertThat(script).identifierReferencesText().containsExactly("alwaysTrueA", "enumField");
        assertThat(script).identifierExpressionsText().containsExactly("EnumTest", "EnumTest","EnumTest");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void and_combined_anyMatch_failure() throws IOException {
        A = DOOV.alwaysTrue("A");
        result = when(A.and(enumField.anyMatch(VAL2, VAL3))).validate().executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("and", "matchAny", "VAL2", "VAL3");
        assertThat(script).identifierReferencesText().containsExactly("alwaysTrueA", "enumField");
        assertThat(script).identifierExpressionsText().containsExactly("EnumTest", "EnumTest");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void matchAny_combined_anyMatch_success() throws IOException {
        A = DOOV.alwaysTrue("A");
        result = when(matchAny(A, enumField.anyMatch(VAL1, VAL2, VAL3))).validate().withShortCircuit(false)
                .executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("matchAny", "matchAny", "VAL1", "VAL2", "VAL3");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "enumField");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueA", "EnumTest", "EnumTest", "EnumTest");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void matchAny_combined_anyMatch_failure() throws IOException {
        A = DOOV.alwaysFalse("A");
        result = when(matchAny(A, enumField.anyMatch(VAL2, VAL3))).validate().executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("matchAny", "matchAny", "VAL2", "VAL3");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "enumField");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysFalseA", "EnumTest", "EnumTest");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
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
    void afterEach() {
        System.out.println(ruleTs);
    }

    enum EnumTest {
        VAL1, VAL2, VAL3;
    }
}
