/*
 * Copyright 2018 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.ts.ast.test;

import static io.doov.assertions.ts.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.sum;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.ts.ast.AstTSRenderer;
import io.doov.ts.ast.writer.DefaultTypeScriptWriter;
import io.doov.ts.ast.writer.TypeScriptWriter;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptSumTest {

    private final GenericModel model = new GenericModel();
    private IntegerFieldInfo A, B;
    private Result result;
    private String ruleTs;

    @Test
    void sum_1_2_greaterThan_1() throws IOException {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        result = when(sum(A, B).greaterThan(1)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("sum", "greaterThan");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("A", "B");
        assertThat(script).literalsText().containsExactly("1");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void sum_1_2_greaterThan_3() throws IOException {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        result = when(sum(A, B).greaterThan(3)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("sum", "greaterThan");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("A", "B");
        assertThat(script).literalsText().containsExactly("3");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void sum_sum_1_sum_2_greaterThan_3() throws IOException {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        result = when(sum(sum(A), sum(B)).greaterThan(3)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = toTS(result.getContext().getRootMetadata());

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("sum", "sum", "sum", "greaterThan");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("A", "B");
        assertThat(script).literalsText().containsExactly("3");
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
