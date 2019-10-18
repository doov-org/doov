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
import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.matchAll;
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
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;
import io.doov.ts.ast.writer.ImportSpec;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptMatchAllTest {

    private StepCondition A, B, C;
    private Result result;
    private String ruleTs;
    
    @RegisterExtension
    static JestExtension jestExtension = new JestExtension("build/jest");

    @Test
    void matchAll_true_true_true() throws IOException {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "matchAll", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueA", "alwaysTrueB", "alwaysTrueC");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void matchAll_true_true_false() throws IOException {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        C = alwaysFalse("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "matchAll", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueA", "alwaysTrueB", "alwaysFalseC");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void matchAll_true_false_false() throws IOException {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "matchAll", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueA", "alwaysFalseB", "alwaysFalseC");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void matchAll_false_false_false() throws IOException {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "matchAll", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysFalseA", "alwaysFalseB", "alwaysFalseC");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void matchAll_field_false_false_false_failure() throws IOException {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo something = model.stringField("something", "string field");
        A = zero.greaterThan(4);
        B = yesterday.after(LocalDateSuppliers.today());
        C = something.matches("^other.*");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "matchAll", "greaterThan", "after", "today",
                "matches", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV", "zero", "yesterday",
                "DateFunction", "stringfield");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().containsExactly("4", "'^other.*'");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @AfterAll
    static void tearDown() {
        jestExtension.getJestTestSpec().getImports().add(new ImportSpec("BooleanFunction", "doov"));
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysTrueA = DOOV.lift(BooleanFunction, true);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysFalseA = DOOV.lift(BooleanFunction, false);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysTrueB = DOOV.lift(BooleanFunction, true);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysFalseB = DOOV.lift(BooleanFunction, false);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysTrueC = DOOV.lift(BooleanFunction, true);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysFalseC = DOOV.lift(BooleanFunction, false);");
        String now = LocalDate.now().minus(1, ChronoUnit.DAYS).toString();
        jestExtension.getJestTestSpec().getBeforeEachs().add("model = { zero: 0, yesterday: new Date('" + now + "'), stringfield: 'something' };");
    }
}
