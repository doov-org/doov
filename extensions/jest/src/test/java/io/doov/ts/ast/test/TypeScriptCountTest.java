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
import static io.doov.ts.ast.test.JestExtension.parseAs;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;
import io.doov.ts.ast.writer.ImportSpec;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptCountTest {

    private StepCondition A, B;
    private Result result;
    private String ruleTs;
    
    @RegisterExtension
    static JestExtension jestExtension = new JestExtension();

    @Test
    void count_false_false() throws IOException {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        result = when(count(A, B).greaterThan(1)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("count", "greaterThan");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysFalseA", "alwaysFalseB");
        assertThat(script).literalsText().containsExactly("1");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void count_true_false_greaterThan() throws IOException {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(count(A, B).greaterThan(1)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("count", "greaterThan");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueA", "alwaysFalseB");
        assertThat(script).literalsText().containsExactly("1");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void count_true_false_greaterOrEquals() throws IOException {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(count(A, B).greaterOrEquals(1)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("count", "greaterOrEquals");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueA", "alwaysFalseB");
        assertThat(script).literalsText().containsExactly("1");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void count_true_true() throws IOException {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        result = when(count(A, B).greaterThan(1)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("count", "greaterThan");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueA", "alwaysTrueB");
        assertThat(script).literalsText().containsExactly("1");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void count_field_true_true_failure() throws IOException {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        result = when(count(A, B).greaterThan(1)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("count", "lesserThan", "before", "greaterThan");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "zero", "yesterday");
        assertThat(script).identifierExpressionsText().containsExactly("today");
        assertThat(script).literalsText().containsExactly("4", "1");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @AfterAll
    static void tearDown() {
        Map<String, String> symbols = new HashMap<>();
        symbols.put("BooleanFunction", null);
        symbols.put("DateFunction", null);
        jestExtension.getJestTestSpec().getImports().add(ImportSpec.starImport("DOOV", "doov"));
        jestExtension.getJestTestSpec().getImports().add(new ImportSpec( "doov", symbols));
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysTrueA = DOOV.lift(BooleanFunction, true);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysFalseA = DOOV.lift(BooleanFunction, false);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysTrueB = DOOV.lift(BooleanFunction, true);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysFalseB = DOOV.lift(BooleanFunction, false);");
        jestExtension.getJestTestSpec().getTestStates().add("const today = DateFunction.today();");
        String now = LocalDate.now().minus(1, ChronoUnit.DAYS).toString();
        jestExtension.getJestTestSpec().getBeforeEachs().add("model = { zero: 0, yesterday: new Date('" + now + "') };");
    }

}
