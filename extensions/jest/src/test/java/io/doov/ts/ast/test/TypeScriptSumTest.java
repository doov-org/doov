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
import static io.doov.ts.ast.test.JestExtension.parseAs;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptSumTest {

    private final GenericModel model = new GenericModel();
    private IntegerFieldInfo A, B;
    private Result result;
    private String ruleTs;
    
    @RegisterExtension
    static JestExtension jestExtension = new JestExtension("build/jest");

    @Test
    void sum_1_2_greaterThan_1() throws IOException {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        result = when(sum(A, B).greaterThan(1)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "sum", "greaterThan", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("A", "B");
        assertThat(script).literalsText().containsExactly("1");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void sum_1_2_greaterThan_3() throws IOException {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        result = when(sum(A, B).greaterThan(3)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "sum", "greaterThan", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("A", "B");
        assertThat(script).literalsText().containsExactly("3");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void sum_sum_1_sum_2_greaterThan_3() throws IOException {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        result = when(sum(sum(A), sum(B)).greaterThan(3)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "sum", "sum", "sum", "greaterThan", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV", "DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("A", "B");
        assertThat(script).literalsText().containsExactly("3");
        assertThat(script).arrayLiteralsText().isEmpty();
    }
    @AfterAll
    static void tearDown() {
        jestExtension.getJestTestSpec().getBeforeEachs().add("model = { A: 1, B:1 };");
    }
}
