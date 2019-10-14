/*
 * Copyright 2018 Courtanet
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
import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.count;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.tsparser.util.TypeScriptParserFactory.parseUsing;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.function.Function;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;
import io.doov.ts.ast.AstTSRenderer;
import io.doov.ts.ast.writer.DefaultTypeScriptWriter;
import io.doov.ts.ast.writer.TypeScriptWriter;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptAndTest {

    private StepCondition A, B, C, D;
    private Result result;
    private String ruleTs;

    @Test
    void and_false_false() throws IOException {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("and");
        assertThat(script).identifierReferencesText().containsExactly("alwaysFalseA");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysFalseB");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void and_true_false() throws IOException {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("and");
        assertThat(script).identifierReferencesText().containsExactly("alwaysTrueA");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysFalseB");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void and_false_true() throws IOException {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("and");
        assertThat(script).identifierReferencesText().containsExactly("alwaysFalseA");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueB");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void and_true_true() throws IOException {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("and");
        assertThat(script).identifierReferencesText().containsExactly("alwaysTrueA");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueB");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void and_field_true_true_failure() throws IOException {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());

        result = when(A.and(B)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("lesserThan", "and", "before");
        assertThat(script).identifierReferencesText().containsExactly("zero", "yesterday");
        assertThat(script).identifierExpressionsText().containsExactly("today");
        assertThat(script).literalsText().containsExactly("4");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void and_and_and() throws IOException {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();

        result = when(A.and(B)
                .and(C)
                .and(D)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("lesserThan", "and", "before", "and", "startsWith",
                "and", "is");
        assertThat(script).identifierReferencesText().containsExactly("zero", "yesterday", "name", "isTrue");
        assertThat(script).identifierExpressionsText().containsExactly("today");
        assertThat(script).literalsText().containsExactly("4", "'B'", "false");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void and_and_count() throws IOException {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();

        result = when(A.and(B.and(count(C, D).greaterThan(1))))
                .validate().withShortCircuit(false).executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("lesserThan", "and", "before", "and", "count",
                "startsWith", "is", "greaterThan");
        assertThat(script).identifierReferencesText().containsExactly("zero", "yesterday", "DOOV", "name", "isTrue");
        assertThat(script).identifierExpressionsText().containsExactly("today");
        assertThat(script).literalsText().containsExactly("4", "'B'", "false", "1");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void and_or_and() throws IOException {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();

        result = when(A.and(B)
                .or(C.and(D)))
                .validate().withShortCircuit(false).executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("lesserThan", "and", "before", "or", "startsWith",
                "and", "is");
        assertThat(script).identifierReferencesText().containsExactly("zero", "yesterday", "name", "isTrue");
        assertThat(script).identifierExpressionsText().containsExactly("today");
        assertThat(script).literalsText().containsExactly("4", "'B'", "false");
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
}
